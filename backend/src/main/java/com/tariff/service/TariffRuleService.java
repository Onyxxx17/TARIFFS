package com.tariff.service;

import java.util.List;

import com.tariff.entity.TariffRule;

public interface TariffRuleService {
    List<TariffRule> listTariffRule();
    TariffRule getTariffRule(Long id);
    TariffRule addTariffRule(TariffRule tariffRule);
    TariffRule updateTariffRule(Long id, TariffRule tariffRule);
    void deleteTariffRule(Long id);
}

