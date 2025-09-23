package com.tariff.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tariff.dto.request.TariffCalculationRequest;
import com.tariff.dto.response.TariffCalculationResponse;
import com.tariff.entity.Country;
import com.tariff.entity.TariffRule;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.TariffRuleRepository;

@Service
public class TariffCalculationService {
    
    @Autowired
    private TariffRuleRepository tariffRuleRepository;

    @Autowired
    private CountryRepository countryRepository;

    public TariffCalculationResponse calculateTariff(TariffCalculationRequest request){
        BigDecimal quantity = new BigDecimal(request.getQuantity());
        BigDecimal importValue = request.getUnitCost().multiply(quantity);
        
        countryRepository.findById(request.getToCountryId())
                        .orElseThrow(() -> new RuntimeException("To country not found"));

        if (request.getFromCountryId() != null) {
            countryRepository.findById(request.getFromCountryId())
                            .orElseThrow(() -> new RuntimeException("From country not found"));
        }

        TariffRule rule = tariffRuleRepository.findApplicableTariffRule(
            request.getFromCountryId(), 
            request.getToCountryId(),   
            request.getProductId(),  
            request.getEffectiveYear()        
        );
        
        if (rule == null){
            return new TariffCalculationResponse(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        BigDecimal rate = rule.getRate();
        BigDecimal calculatedTariff = importValue.add(importValue.multiply(rate));

        return new TariffCalculationResponse(rate, calculatedTariff);
    }
}       
