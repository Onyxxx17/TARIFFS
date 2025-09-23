package com.tariff.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tariff.dto.request.TariffCalculationRequest;
import com.tariff.dto.response.TariffCalculationResponse;
import com.tariff.entity.Country;
import com.tariff.entity.TariffRule;
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

        if (fromCountryOpt.isEmpty() || toCountryOpt.isEmpty()) {
            throw new RuntimeException("Country not found");
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
        BigDecimal tariff = importValue.multiply(rate).divide(BigDecimal.valueOf(100));
        BigDecimal totalCost = importValue.add(tariff);

        return new TariffCalculationResponse(request.getFromCountry(),request.getToCountry(),rate, totalCost);
    }
}
