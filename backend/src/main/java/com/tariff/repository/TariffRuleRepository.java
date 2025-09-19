package com.tariff.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.TariffRule;

public interface TariffRuleRepository extends JpaRepository<TariffRule, Long> {
    // For many-to-many relationship with countries
    List<TariffRule> findByCountriesId(Long countryId);
    List<TariffRule> findByProductId(Long productId);
    List<TariffRule> findByCountriesIdAndProductId(Long countryId, Long productId);
    Optional<TariffRule> findByIdAndProductId(Long id, Long productId);
    List<TariffRule> findByEffectiveDateBetween(LocalDate startDate, LocalDate endDate);
    List<TariffRule> findByCountriesIdAndEffectiveDateBetween(Long countryId, LocalDate startDate, LocalDate endDate);
}