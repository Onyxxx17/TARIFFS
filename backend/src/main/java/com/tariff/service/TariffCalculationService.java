package com.tariff.service;

import java.math.BigDecimal;
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
        BigDecimal additionalFee = rule.getAdditionalFee() != null ? rule.getAdditionalFee() : BigDecimal.ZERO;
        
        // Calculate percentage-based tariff
        BigDecimal percentageTariff = importValue.multiply(rate).divide(BigDecimal.valueOf(100));
        
        // Total tariff includes both percentage-based tariff and additional fees
        BigDecimal totalTariff = percentageTariff.add(additionalFee);
        BigDecimal totalCost = importValue.add(totalTariff);

        return new TariffCalculationResponse(request.getFromCountry(),request.getToCountry(),rate, totalTariff, additionalFee, totalCost);
    }
}
