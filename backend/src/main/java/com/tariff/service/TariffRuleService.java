package com.tariff.service;

import java.util.List;

import com.tariff.entity.TariffRule;

public interface TariffRuleService {

    List<TariffRule> listTariffRule();

    TariffRule getTariffRule(Long id);

    List<TariffRule> getTariffRulesByFromCountryCode(String fromCountryCode);
    
    List<TariffRule> getTariffRulesByToCountryCode(String toCountryCode);
    
    List<TariffRule> getTariffRulesByCountryCode(String countryCode); // Returns rules where country is either from or to

    List<TariffRule> getTariffRulesByProductId(Long productId);

    TariffRule addTariffRule(TariffRule tariffRule);

    TariffRule addTariffRuleByCountriesAndProduct(String fromCountryCode, String toCountryCode, Long productId, TariffRule tariffRule);

    TariffRule updateTariffRule(Long id, TariffRule tariffRule);

    TariffRule updateTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id, TariffRule tariffRule);

    void deleteTariffRule(Long id);

    void deleteTariffRule(String fromCountryCode, String toCountryCode, Long productId, Long id);
}