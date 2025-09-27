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

    long count();
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

    @Query("SELECT tr FROM TariffRule tr " +
       "LEFT JOIN tr.fromCountry fc " +
       "LEFT JOIN tr.toCountry tc " +
       "LEFT JOIN tr.product p " +
       "WHERE (:fromCountryName IS NULL OR :fromCountryName = '' OR fc.name = :fromCountryName) " +
       "AND (:toCountryName IS NULL OR :toCountryName = '' OR tc.name = :toCountryName) " +
       "AND (:effectiveYear IS NULL OR tr.effectiveYear = :effectiveYear) " +
       "AND (:productName IS NULL OR :productName = '' OR p.name = :productName) " +
       "AND (:productId IS NULL OR p.id = :productId)")
        List<TariffRule> findByMultipleCriteria(@Param("fromCountryName") String fromCountryName,
                                            @Param("toCountryName") String toCountryName,
                                            @Param("effectiveYear") Integer effectiveYear,
                                            @Param("productName") String productName,
                                            @Param("productId") Long productId);
   
}