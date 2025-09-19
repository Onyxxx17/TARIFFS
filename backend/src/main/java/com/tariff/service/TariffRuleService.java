package com.tariff.service;

import java.util.List;

import com.tariff.entity.TariffRule;

public interface TariffRuleService {

    List<TariffRule> listTariffRule();

    TariffRule getTariffRule(Long id);

    List<TariffRule> getTariffRulesByCountryId(Long countryId);

    List<TariffRule> getTariffRulesByProductId(Long productId);

    TariffRule addTariffRule(TariffRule tariffRule);

    TariffRule addTariffRuleByCountryAndProduct(Long countryId, Long productId, TariffRule tariffRule);

    TariffRule updateTariffRule(Long id, TariffRule tariffRule);

    TariffRule updateTariffRule(Long countryId, Long productId, Long id, TariffRule tariffRule);

    void deleteTariffRule(Long id);

    void deleteTariffRule(Long countryId, Long productId, Long id);
}
