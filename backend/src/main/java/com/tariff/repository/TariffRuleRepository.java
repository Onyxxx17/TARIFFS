package com.tariff.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tariff.entity.Country;
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

    @Query(value = """
        SELECT * FROM tariff_rule
        WHERE (from_country_id = :fromCountryId OR from_country_id IS NULL)
          AND to_country_id = :toCountryId
          AND product_id = :productId
          AND effective_year = :effectiveYear
        ORDER BY CASE WHEN from_country_id IS NOT NULL THEN 1 ELSE 2 END
        LIMIT 1
        """, nativeQuery = true)
    TariffRule findApplicableTariffRule(
        @Param("fromCountryId") String fromCountryId,
        @Param("toCountryId") String toCountryId,
        @Param("productId") Long productId,
        @Param("effectiveYear") Integer effectiveYear
    );
}