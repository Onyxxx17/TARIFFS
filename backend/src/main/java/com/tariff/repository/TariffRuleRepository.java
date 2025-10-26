package com.tariff.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tariff.dto.response.TariffRateOverTimeDTO;
import com.tariff.entity.Country;
import com.tariff.entity.TariffRule;

public interface TariffRuleRepository extends JpaRepository<TariffRule, Long> {
    // For many-to-one relationship with fromCountry and toCountry

    long count();
    Page<TariffRule> findByFromCountryCountryCode(String fromCountryCode, Pageable pageable);
    Page<TariffRule> findByToCountryCountryCode(String toCountryCode, Pageable pageable);
    Page<TariffRule> findByFromCountryCountryCodeOrToCountryCountryCode(String fromCountryCode, String toCountryCode, Pageable pageable);
    Page<TariffRule> findByFromCountryCountryCodeAndToCountryCountryCode(String fromCountryCode, String toCountryCode, Pageable pageable);
    
    Page<TariffRule> findByProductId(Long productId, Pageable pageable);
    Page<TariffRule> findByFromCountryCountryCodeAndProductId(String fromCountryCode, Long productId, Pageable pageable);
    Page<TariffRule> findByToCountryCountryCodeAndProductId(String toCountryCode, Long productId, Pageable pageable);
    Page<TariffRule> findByFromCountryCountryCodeAndToCountryCountryCodeAndProductId(String fromCountryCode, String toCountryCode, Long productId, Pageable pageable);
    
    Optional<TariffRule> findByIdAndProductId(Long id, Long productId);
    Page<TariffRule> findByEffectiveYearBetween(int startYear, int endYear, Pageable pageable);
    Page<TariffRule> findByFromCountryCountryCodeAndEffectiveYearBetween(String fromCountryCode, int startYear, int endYear, Pageable pageable);
    Page<TariffRule> findByToCountryCountryCodeAndEffectiveYearBetween(String toCountryCode, int startYear, int endYear, Pageable pageable);

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
    
    // Priority: 1. Specific bilateral rate (from_country matches), 2. General rate (from_country IS NULL)
    @Query("SELECT new com.tariff.dto.response.TariffRateOverTimeDTO(t.effectiveYear, t.rate) " +
           "FROM TariffRule t " +
           "WHERE t.toCountry.countryCode = :toCountryCode " +
           "AND t.product.id = :productId " +
           "AND (t.fromCountry.countryCode = :fromCountryCode " +
           "     OR (t.fromCountry IS NULL AND NOT EXISTS (" +
           "         SELECT 1 FROM TariffRule t2 " +
           "         WHERE t2.toCountry.countryCode = :toCountryCode " +
           "         AND t2.product.id = :productId " +
           "         AND t2.fromCountry.countryCode = :fromCountryCode " +
           "         AND t2.effectiveYear = t.effectiveYear))) " +
           "ORDER BY t.effectiveYear ASC")
    List<TariffRateOverTimeDTO> findTariffRatesOverTime(
        @Param("fromCountryCode") String fromCountryCode,
        @Param("toCountryCode") String toCountryCode,
        @Param("productId") Long productId
    );
}