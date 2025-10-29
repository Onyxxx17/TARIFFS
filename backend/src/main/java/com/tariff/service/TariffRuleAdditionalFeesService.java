package com.tariff.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.TariffRule;
import com.tariff.exception.TariffRuleNotFoundException;
import com.tariff.repository.TariffRuleAdditionalFeesRepository;
import com.tariff.repository.TariffRuleRepository;

@Service
@Transactional
public class TariffRuleAdditionalFeesService {
    
    @Autowired
    private TariffRuleAdditionalFeesRepository additionalFeesRepository;
    
    @Autowired
    private TariffRuleRepository tariffRuleRepository;
    
    public List<BigDecimal> getAdditionalFeesByTariffRuleId(Long tariffRuleId) {
        if (!tariffRuleRepository.existsById(tariffRuleId)) {
            throw new TariffRuleNotFoundException(tariffRuleId);
        }
        return additionalFeesRepository.findAdditionalFeesByTariffRuleId(tariffRuleId);
    }
    
    public List<Map<String, Object>> getAllAdditionalFeesEntries() {
        List<Object[]> results = additionalFeesRepository.findAllAdditionalFeesEntries();
        List<Map<String, Object>> entries = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("tariffRuleId", result[0]);
            entry.put("additionalFeeRate", result[1]);
            entries.add(entry);
        }
        
        return entries;
    }
    
    public List<BigDecimal> getAllUniqueAdditionalFeeRates() {
        return additionalFeesRepository.findAllUniqueAdditionalFeeRates();
    }
    
    public List<Map<String, Object>> getAdditionalFeeUsageStatistics() {
        List<Object[]> results = additionalFeesRepository.findAdditionalFeeUsageStatistics();
        List<Map<String, Object>> statistics = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("feeRate", result[0]);
            stat.put("usageCount", result[1]);
            statistics.add(stat);
        }
        
        return statistics;
    }
    
    public List<Map<String, Object>> getAllAdditionalFeesWithDetails() {
        List<Object[]> results = additionalFeesRepository.findAllAdditionalFeesWithDetails();
        List<Map<String, Object>> detailedEntries = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("tariffRuleId", result[0]);
            entry.put("additionalFeeRate", result[1]);
            entry.put("baseTariffRate", result[2]);
            entry.put("effectiveYear", result[3]);
            entry.put("fromCountryName", result[4]);
            entry.put("toCountryName", result[5]);
            entry.put("productName", result[6]);
            detailedEntries.add(entry);
        }
        
        return detailedEntries;
    }
    
    public void addAdditionalFeeToTariffRule(Long tariffRuleId, BigDecimal feeRate) {
        TariffRule tariffRule = tariffRuleRepository.findById(tariffRuleId)
            .orElseThrow(() -> new TariffRuleNotFoundException(tariffRuleId));
        
        tariffRule.getAdditionalFees().add(feeRate);
        tariffRuleRepository.save(tariffRule);
    }
    
    public void removeAdditionalFeeFromTariffRule(Long tariffRuleId, BigDecimal feeRate) {
        TariffRule tariffRule = tariffRuleRepository.findById(tariffRuleId)
            .orElseThrow(() -> new TariffRuleNotFoundException(tariffRuleId));
        
        tariffRule.getAdditionalFees().remove(feeRate);
        tariffRuleRepository.save(tariffRule);
    }
    
    public void clearAllAdditionalFeesForTariffRule(Long tariffRuleId) {
        TariffRule tariffRule = tariffRuleRepository.findById(tariffRuleId)
            .orElseThrow(() -> new TariffRuleNotFoundException(tariffRuleId));
        
        tariffRule.getAdditionalFees().clear();
        tariffRuleRepository.save(tariffRule);
    }
    
    public List<TariffRule> getTariffRulesWithCarbonTax() {
        return additionalFeesRepository.findTariffRulesWithSpecificAdditionalFee(new BigDecimal("27.5"));
    }
    
    public List<TariffRule> getTariffRulesWithSanitaryBarriers() {
        return additionalFeesRepository.findTariffRulesWithSpecificAdditionalFee(new BigDecimal("16.4"));
    }
}
