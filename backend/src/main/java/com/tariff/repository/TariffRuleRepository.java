package com.tariff.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.TariffRule;

public interface TariffRuleRepository extends JpaRepository<TariffRule, Long> {
    // For many-to-one relationship with fromCountry and toCountry
    List<TariffRule> findByFromCountryId(Long fromCountryId);
    List<TariffRule> findByToCountryId(Long toCountryId);
    List<TariffRule> findByFromCountryIdOrToCountryId(Long fromCountryId, Long toCountryId);
    List<TariffRule> findByFromCountryIdAndToCountryId(Long fromCountryId, Long toCountryId);
    
    List<TariffRule> findByProductId(Long productId);
    List<TariffRule> findByFromCountryIdAndProductId(Long fromCountryId, Long productId);
    List<TariffRule> findByToCountryIdAndProductId(Long toCountryId, Long productId);
    List<TariffRule> findByFromCountryIdAndToCountryIdAndProductId(Long fromCountryId, Long toCountryId, Long productId);
    
    Optional<TariffRule> findByIdAndProductId(Long id, Long productId);
    List<TariffRule> findByEffectiveYearBetween(int startYear, int endYear);
    List<TariffRule> findByFromCountryIdAndEffectiveYearBetween(Long fromCountryId, int startYear, int endYear);
    List<TariffRule> findByToCountryIdAndEffectiveYearBetween(Long toCountryId, int startYear, int endYear);
}