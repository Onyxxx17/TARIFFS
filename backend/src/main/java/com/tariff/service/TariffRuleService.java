package com.tariff.service;

import java.util.List;

import com.tariff.entity.TariffRule;

public interface TariffRuleService {

    List<TariffRule> listTariffRule();

    TariffRule getTariffRule(Long id);

    List<TariffRule> getTariffRulesByFromCountryId(Long fromCountryId);
    
    List<TariffRule> getTariffRulesByToCountryId(Long toCountryId);
    
    List<TariffRule> getTariffRulesByCountryId(Long countryId); // Returns rules where country is either from or to

    List<TariffRule> getTariffRulesByProductId(Long productId);

    TariffRule addTariffRule(TariffRule tariffRule);

    TariffRule addTariffRuleByCountriesAndProduct(Long fromCountryId, Long toCountryId, Long productId, TariffRule tariffRule);

    TariffRule updateTariffRule(Long id, TariffRule tariffRule);

    TariffRule updateTariffRule(Long fromCountryId, Long toCountryId, Long productId, Long id, TariffRule tariffRule);

    void deleteTariffRule(Long id);

    void deleteTariffRule(Long fromCountryId, Long toCountryId, Long productId, Long id);
}