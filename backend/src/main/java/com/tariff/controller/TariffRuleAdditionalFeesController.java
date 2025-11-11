package com.tariff.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tariff.entity.TariffRule;
import com.tariff.service.TariffRuleAdditionalFeesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/tariff-rules/additional-fees")
public class TariffRuleAdditionalFeesController {
    
    @Autowired
    private TariffRuleAdditionalFeesService additionalFeesService;
    
    @Operation(summary = "Get additional fees for a tariff rule")
    @GetMapping("/tariff-rule/{tariffRuleId}")
    public ResponseEntity<List<BigDecimal>> getAdditionalFees(@PathVariable Long tariffRuleId) {
        List<BigDecimal> fees = additionalFeesService.getAdditionalFeesByTariffRuleId(tariffRuleId);
        return ResponseEntity.ok(fees);
    }
    
    @Operation(summary = "Add additional fee to a tariff rule")
    @PostMapping("/tariff-rule/{tariffRuleId}")
    public ResponseEntity<String> addAdditionalFee(
            @PathVariable Long tariffRuleId, 
            @RequestParam BigDecimal feeRate) {
        additionalFeesService.addAdditionalFeeToTariffRule(tariffRuleId, feeRate);
        return ResponseEntity.ok("Additional fee added successfully");
    }
    
    @Operation(summary = "Remove specific additional fee from a tariff rule")
    @DeleteMapping("/tariff-rule/{tariffRuleId}")
    public ResponseEntity<String> removeAdditionalFee(
            @PathVariable Long tariffRuleId, 
            @RequestParam BigDecimal feeRate) {
        additionalFeesService.removeAdditionalFeeFromTariffRule(tariffRuleId, feeRate);
        return ResponseEntity.ok("Additional fee removed successfully");
    }
    
    @Operation(summary = "Clear all additional fees for a tariff rule")
    @DeleteMapping("/tariff-rule/{tariffRuleId}/clear")
    public ResponseEntity<String> clearAllAdditionalFees(@PathVariable Long tariffRuleId) {
        additionalFeesService.clearAllAdditionalFeesForTariffRule(tariffRuleId);
        return ResponseEntity.ok("All additional fees cleared successfully");
    }
    
    @Operation(summary = "Get all tariff rules with carbon tax (27.5%)")
    @GetMapping("/carbon-tax")
    public ResponseEntity<List<TariffRule>> getTariffRulesWithCarbonTax() {
        List<TariffRule> rules = additionalFeesService.getTariffRulesWithCarbonTax();
        return ResponseEntity.ok(rules);
    }
    
    @Operation(summary = "Get all tariff rules with sanitary barriers (16.4%)")
    @GetMapping("/sanitary-barriers")
    public ResponseEntity<List<TariffRule>> getTariffRulesWithSanitaryBarriers() {
        List<TariffRule> rules = additionalFeesService.getTariffRulesWithSanitaryBarriers();
        return ResponseEntity.ok(rules);
    }
    
    @Operation(summary = "Get all additional fee entries across all tariff rules")
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllAdditionalFeesEntries() {
        List<Map<String, Object>> entries = additionalFeesService.getAllAdditionalFeesEntries();
        return ResponseEntity.ok(entries);
    }
    
    @Operation(summary = "Get all unique additional fee rates")
    @GetMapping("/unique-rates")
    public ResponseEntity<List<BigDecimal>> getAllUniqueAdditionalFeeRates() {
        List<BigDecimal> rates = additionalFeesService.getAllUniqueAdditionalFeeRates();
        return ResponseEntity.ok(rates);
    }
    
    @Operation(summary = "Get usage statistics for additional fees")
    @GetMapping("/statistics")
    public ResponseEntity<List<Map<String, Object>>> getAdditionalFeeUsageStatistics() {
        List<Map<String, Object>> stats = additionalFeesService.getAdditionalFeeUsageStatistics();
        return ResponseEntity.ok(stats);
    }
    
    @Operation(summary = "Get all additional fees with complete tariff rule details")
    @GetMapping("/detailed")
    public ResponseEntity<List<Map<String, Object>>> getAllAdditionalFeesWithDetails() {
        List<Map<String, Object>> detailedEntries = additionalFeesService.getAllAdditionalFeesWithDetails();
        return ResponseEntity.ok(detailedEntries);
    }
}
