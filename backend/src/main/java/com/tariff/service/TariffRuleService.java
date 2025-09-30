package com.tariff.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tariff.entity.TariffRule;

public interface TariffRuleService {

    Page<TariffRule> listTariffRule(Pageable pageable);

    TariffRule getTariffRule(Long id);

    Page<TariffRule> getTariffRulesByFromCountryCode(String fromCountryCode, Pageable pageable);
    
    Page<TariffRule> getTariffRulesByToCountryCode(String toCountryCode, Pageable pageable);
    
    Page<TariffRule> getTariffRulesByCountryCode(String countryCode, Pageable pageable); // Returns rules where country is either from or to

    Page<TariffRule> getTariffRulesByProductId(Long productId, Pageable pageable);

    TariffRule addTariffRule(TariffRule tariffRule);

    TariffRule addTariffRuleByCountriesAndProduct(String fromCountryCode, String toCountryCode, Long productId, TariffRule tariffRule);

    TariffRule updateTariffRule(Long id, TariffRule tariffRule);

    TariffRule updateTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id, TariffRule tariffRule);

    void deleteTariffRule(Long id);

    void deleteTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id);
}