package com.tariff.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tariff.dto.response.TariffComparisonDTO;
import com.tariff.dto.response.TariffRateOverTimeDTO;
import com.tariff.entity.TariffRule;

public interface TariffRuleService {

    Page<TariffRule> listTariffRule(Pageable pageable);

    TariffRule getTariffRule(Long id);

    Page<TariffRule> getTariffRulesByToCountryCode(String toCountryCode, Pageable pageable);

    Page<TariffRule> getTariffRulesByCountryCode(String countryCode, Pageable pageable); // Returns rules where country is either from or to

    Page<TariffRule> getTariffRulesByProductId(Long productId, Pageable pageable);

    TariffRule addTariffRule(TariffRule tariffRule);

    TariffRule addTariffRuleByCountriesAndProduct(String fromCountryCode, String toCountryCode, Long productId, TariffRule tariffRule);

    TariffRule updateTariffRule(Long id, TariffRule tariffRule);

    TariffRule updateTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id, TariffRule tariffRule);

    void deleteTariffRule(Long id);

    void deleteTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id);

    List<TariffRateOverTimeDTO> getTariffRatesOverTime(String fromCountryCode, String toCountryCode, Long productId);

    TariffComparisonDTO compareTariffRates(String country1Code, String country2Code, Long productId);

    List<TariffRule> getTariffRulesByCountriesAndProduct(String fromCountryCode, String toCountryCode, Long productId);
}
