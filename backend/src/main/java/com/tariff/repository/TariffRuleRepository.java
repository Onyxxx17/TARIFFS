package com.tariff.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.TariffRule;

public interface TariffRuleRepository extends JpaRepository<TariffRule, Long> {
    // For many-to-one relationship with fromCountry and toCountry
    List<TariffRule> findByFromCountryCountryCode(String fromCountryCode);
    List<TariffRule> findByToCountryCountryCode(String toCountryCode);
    List<TariffRule> findByFromCountryCountryCodeOrToCountryCountryCode(String fromCountryCode, String toCountryCode);
    List<TariffRule> findByFromCountryCountryCodeAndToCountryCountryCode(String fromCountryCode, String toCountryCode);
    
    List<TariffRule> findByProductId(Long productId);
    List<TariffRule> findByFromCountryCountryCodeAndProductId(String fromCountryCode, Long productId);
    List<TariffRule> findByToCountryCountryCodeAndProductId(String toCountryCode, Long productId);
    List<TariffRule> findByFromCountryCountryCodeAndToCountryCountryCodeAndProductId(String fromCountryCode, String toCountryCode, Long productId);
    
    Optional<TariffRule> findByIdAndProductId(Long id, Long productId);
    List<TariffRule> findByEffectiveYearBetween(int startYear, int endYear);
    List<TariffRule> findByFromCountryCountryCodeAndEffectiveYearBetween(String fromCountryCode, int startYear, int endYear);
    List<TariffRule> findByToCountryCountryCodeAndEffectiveYearBetween(String toCountryCode, int startYear, int endYear);
}