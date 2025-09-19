package com.tariff.controller;

import com.tariff.entity.TariffRule;
import com.tariff.service.TariffRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariff-rules")
public class TariffRuleController {
    
    private TariffRuleService tariffRuleService;
    
    public TariffRuleController(TariffRuleService tariffRuleService) {
        this.tariffRuleService = tariffRuleService;
    }
    
    @GetMapping
    public List<TariffRule> getAllTariffRules() {
        return tariffRuleService.listTariffRule();
    }
    
    @GetMapping("/{id}")
    public TariffRule getTariffRuleById(@PathVariable Long id) {
        return tariffRuleService.getTariffRule(id);
    }
    
    @GetMapping("/country/{countryId}")
    public List<TariffRule> getTariffRulesByCountry(@PathVariable Long countryId) {
        return tariffRuleService.getTariffRulesByCountryId(countryId);
    }
    
    @GetMapping("/product/{productId}")
    public List<TariffRule> getTariffRulesByProduct(@PathVariable Long productId) {
        return tariffRuleService.getTariffRulesByProductId(productId);
    }
    
    @PostMapping
    public TariffRule createTariffRule(@RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRule(tariffRule);
    }
    
    @PostMapping("/country/{countryId}/product/{productId}")
    public TariffRule createTariffRuleWithCountryAndProduct(
            @PathVariable Long countryId,
            @PathVariable Long productId,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRuleByCountryAndProduct(countryId, productId, tariffRule);
    }
    
    @PutMapping("/{id}")
    public TariffRule updateTariffRule(@PathVariable Long id, @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(id, tariffRule);
    }
    
    @PutMapping("/country/{countryId}/product/{productId}/{id}")
    public TariffRule updateTariffRuleWithCountryAndProduct(
            @PathVariable Long countryId,
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(countryId, productId, id, tariffRule);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTariffRule(@PathVariable Long id) {
        tariffRuleService.deleteTariffRule(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/country/{countryId}/product/{productId}/{id}")
    public ResponseEntity<?> deleteTariffRuleWithCountryAndProduct(
            @PathVariable Long countryId,
            @PathVariable Long productId,
            @PathVariable Long id) {
        tariffRuleService.deleteTariffRule(countryId, productId, id);
        return ResponseEntity.ok().build();
    }
}