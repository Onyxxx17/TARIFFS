package com.tariff.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tariff.dto.request.TariffCalculationRequest;
import com.tariff.dto.response.TariffCalculationResponse;
import com.tariff.entity.Country;
import com.tariff.entity.TariffRule;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.exception.TariffRuleNotFoundException;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.TariffRuleRepository;

@Service
public class TariffCalculationService {

    @Autowired
    private TariffRuleRepository tariffRuleRepository;

    @Autowired
    private CountryRepository countryRepository;

    public TariffCalculationResponse calculateTariff(TariffCalculationRequest request) {
        BigDecimal quantity = new BigDecimal(request.getQuantity());
        BigDecimal importValue = request.getUnitCost().multiply(quantity);

        Optional<Country> fromCountryOpt = countryRepository.findByName(request.getFromCountry());
        Optional<Country> toCountryOpt = countryRepository.findByName(request.getToCountry());

        if (fromCountryOpt.isEmpty()) {
            throw new CountryNotFoundException(request.getFromCountry());
        }

        if(toCountryOpt.isEmpty()){
            throw new CountryNotFoundException(request.getToCountry());
        }
        
        String fromCountryId = fromCountryOpt.get().getCountryCode();
        String toCountryId = toCountryOpt.get().getCountryCode();

        TariffRule rule = tariffRuleRepository.findApplicableTariffRule(
                fromCountryId,
                toCountryId,
                request.getProductId(),
                request.getEffectiveYear()
        );

        if (rule == null) {
            throw new TariffRuleNotFoundException(request.getFromCountry(),request.getToCountry(),request.getEffectiveYear(),request.getProductId());
        }

        BigDecimal rate = rule.getRate();
        
        // Ensure additional fees are loaded from the database table
        // The @ElementCollection should automatically fetch this, but let's make sure it's initialized
        List<BigDecimal> additionalFees = rule.getAdditionalFees();
        if (additionalFees == null) {
            additionalFees = new ArrayList<>();
        }
        
        // Debug logging to see what additional fees are being applied
        System.out.println("Tariff Rule ID: " + rule.getId());
        System.out.println("Base tariff rate: " + rate + "%");
        System.out.println("Additional fees from database: " + additionalFees);
        System.out.println("Applying additional fees for " + request.getFromCountry() + " -> " + request.getToCountry() + ": " + additionalFees);
        
        BigDecimal hundred = new BigDecimal("100");
        
        // Calculate base tariff (percentage-based on import value)
        BigDecimal baseTariffAmount = importValue.multiply(rate.divide(hundred, 10, RoundingMode.HALF_UP));
        
        // Calculate additional fees (each applied to import value)
        BigDecimal totalAdditionalFeesAmount = BigDecimal.ZERO;
        for (BigDecimal feeRate : additionalFees) {
            BigDecimal feeAmount = importValue.multiply(feeRate.divide(hundred, 10, RoundingMode.HALF_UP));
            totalAdditionalFeesAmount = totalAdditionalFeesAmount.add(feeAmount);
            System.out.println("Applied additional fee " + feeRate + "%: $" + feeAmount + " (applied to import value: $" + importValue + ")");
        }
        
        // Total tariff = base tariff + all additional fees
        BigDecimal totalTariff = baseTariffAmount.add(totalAdditionalFeesAmount);
        BigDecimal totalCost = importValue.add(totalTariff);

        System.out.println("Calculation summary:");
        System.out.println("  Import value: $" + importValue);
        System.out.println("  Base tariff (" + rate + "%): $" + baseTariffAmount);
        System.out.println("  Total additional fees: $" + totalAdditionalFeesAmount);
        System.out.println("  Total tariff: $" + totalTariff);
        System.out.println("  Final total cost: $" + totalCost);

        return new TariffCalculationResponse(request.getFromCountry(),request.getToCountry(),rate, totalTariff, totalAdditionalFeesAmount, totalCost, additionalFees);
    }
}
