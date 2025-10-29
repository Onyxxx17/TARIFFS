package com.tariff.service;

import com.tariff.dto.request.TariffPredictionRequest;
import com.tariff.dto.response.TariffPredictionResponse;
import com.tariff.dto.response.TariffRateOverTimeDTO;
import com.tariff.entity.TariffRule;
import com.tariff.repository.TariffRuleRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TariffPredictionService {

    private final TariffRuleRepository tariffRuleRepository;

    public TariffPredictionService(TariffRuleRepository tariffRuleRepository) {
        this.tariffRuleRepository = tariffRuleRepository;
    }

   public TariffPredictionResponse predictFutureTariffRate(TariffPredictionRequest request) {
        String fromCountryCode = request.getFromCountryCode();
        String toCountryCode = request.getToCountryCode();
        Long productId = request.getProductId();
        int targetYear = request.getYear();

        // Fetch all tariff rules for the toCountry + product
        List<TariffRule> allRules = tariffRuleRepository.findByToCountryAndProduct(toCountryCode, productId);
        if (allRules.isEmpty()) {
            throw new RuntimeException("No tariff data found for product " + productId + " to country " + toCountryCode);
        }

        // Split into specific vs default (NULL from_country)
        List<TariffRule> specificRules = allRules.stream()
            .filter(r -> r.getFromCountry() != null &&
                         fromCountryCode.equals(r.getFromCountry().getCountryCode()))
            .collect(Collectors.toList());

        List<TariffRule> defaultRules = allRules.stream()
            .filter(r -> r.getFromCountry() == null)
            .collect(Collectors.toList());

        // Combine years and build per-year rate map with fallback
        Map<Integer, BigDecimal> yearToRate = new HashMap<>();
        Set<Integer> allYears = allRules.stream()
            .map(TariffRule::getEffectiveYear)
            .collect(Collectors.toSet());

        for (Integer year : allYears) {
            TariffRule specific = specificRules.stream()
                .filter(r -> r.getEffectiveYear() == year)
                .findFirst()
                .orElse(null);

            if (specific != null) {
                yearToRate.put(year, specific.getRate());
            } else {
                TariffRule fallback = defaultRules.stream()
                    .filter(r -> r.getEffectiveYear() == year)
                    .findFirst()
                    .orElse(null);

                if (fallback != null) {
                    yearToRate.put(year, fallback.getRate());
                }
            }
        }

        // Make sure there’s enough data
        if (yearToRate.size() < 2) {
            throw new RuntimeException("Not enough historical data to predict future rates.");
        }

        // Build regression model
        SimpleRegression regression = new SimpleRegression();
        yearToRate.forEach((year, rate) -> regression.addData(year, rate.doubleValue()));

        // Predict target year
        double predictedValue = regression.predict(targetYear);
        if (Double.isNaN(predictedValue) || predictedValue < 0) predictedValue = 0.0;

        // Calculate model fit (R²)
        double modelFit = regression.getRSquare();

        return new TariffPredictionResponse(
            fromCountryCode,
            toCountryCode,
            productId,
            targetYear,
            BigDecimal.valueOf(predictedValue),
            modelFit
        );
    }
}
