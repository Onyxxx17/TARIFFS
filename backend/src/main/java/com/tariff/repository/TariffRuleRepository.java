package com.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.TariffRule;


public interface TariffRuleRepository extends JpaRepository<TariffRule, Long> {
    
}
