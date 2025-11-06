package com.tariff.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tariff.entity.TariffRule;

public interface TariffRuleAdditionalFeesRepository extends JpaRepository<TariffRule, Long> {
    
    @Query(value = "SELECT additional_fee_rate FROM tariff_rule_additional_fees WHERE tariff_rule_id = :tariffRuleId", nativeQuery = true)
    List<BigDecimal> findAdditionalFeesByTariffRuleId(@Param("tariffRuleId") Long tariffRuleId);
    
    // Get all additional fee entries from all tariff rules
    @Query(value = "SELECT tariff_rule_id, additional_fee_rate FROM tariff_rule_additional_fees ORDER BY tariff_rule_id", nativeQuery = true)
    List<Object[]> findAllAdditionalFeesEntries();
    
    // Get all unique additional fee rates across all tariff rules
    @Query(value = "SELECT DISTINCT additional_fee_rate FROM tariff_rule_additional_fees ORDER BY additional_fee_rate", nativeQuery = true)
    List<BigDecimal> findAllUniqueAdditionalFeeRates();
    
    // Get count of how many times each fee rate is used
    @Query(value = "SELECT additional_fee_rate, COUNT(*) as usage_count FROM tariff_rule_additional_fees GROUP BY additional_fee_rate ORDER BY usage_count DESC", nativeQuery = true)
    List<Object[]> findAdditionalFeeUsageStatistics();
    
    // Get all additional fees with tariff rule details
    @Query(value = """
        SELECT traf.tariff_rule_id, traf.additional_fee_rate, 
               tr.rate as base_tariff_rate, tr.effective_year,
               fc.name as from_country_name, tc.name as to_country_name,
               p.name as product_name
        FROM tariff_rule_additional_fees traf
        JOIN tariff_rule tr ON traf.tariff_rule_id = tr.id
        LEFT JOIN country fc ON tr.from_country_id = fc.country_code
        JOIN country tc ON tr.to_country_id = tc.country_code
        JOIN product p ON tr.product_id = p.id
        ORDER BY traf.tariff_rule_id, traf.additional_fee_rate
        """, nativeQuery = true)
    List<Object[]> findAllAdditionalFeesWithDetails();
    
    @Query(value = "SELECT tr.* FROM tariff_rule tr INNER JOIN tariff_rule_additional_fees traf ON tr.id = traf.tariff_rule_id WHERE traf.additional_fee_rate = :feeRate", nativeQuery = true)
    List<TariffRule> findTariffRulesWithSpecificAdditionalFee(@Param("feeRate") BigDecimal feeRate);
}
