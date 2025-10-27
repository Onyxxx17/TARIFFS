package com.tariff.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        // countryRepository.findById(request.getToCountry())
        //         .orElseThrow(() -> new RuntimeException("To country not found"));

        // if (request.getFromCountry() != null) {
        //     countryRepository.findById(request.getFromCountry())
        //             .orElseThrow(() -> new RuntimeException("From country not found"));
        // }

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
        List<BigDecimal> additionalFees = rule.getAdditionalFees() != null ? rule.getAdditionalFees() : new java.util.ArrayList<>();
        
        BigDecimal hundred = new BigDecimal("100");
        
        // Start with import value
        BigDecimal costAfterTariff = importValue.multiply(BigDecimal.ONE.add(rate.divide(hundred, 10, RoundingMode.HALF_UP)));

        BigDecimal currentTotal = costAfterTariff;
        for (BigDecimal feeRate : additionalFees) {
            currentTotal = currentTotal.multiply(BigDecimal.ONE.add(feeRate.divide(hundred, 10, RoundingMode.HALF_UP)));
        }

        BigDecimal totalCost = currentTotal;
        BigDecimal totalTariff = totalCost.subtract(importValue);
        BigDecimal totalAdditionalFeesAmount = totalCost.subtract(costAfterTariff);


        return new TariffCalculationResponse(request.getFromCountry(),request.getToCountry(),rate, totalTariff, totalAdditionalFeesAmount, totalCost, additionalFees);
    }
}
