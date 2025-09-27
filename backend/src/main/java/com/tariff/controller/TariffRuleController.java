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
    
    // Backward compatibility - returns rules where country is either from or to
    @GetMapping("/country/{countryCode}")
    public List<TariffRule> getTariffRulesByCountry(@PathVariable String countryCode) {
        return tariffRuleService.getTariffRulesByCountryCode(countryCode);
    }
    
    // New endpoint for from country
    @GetMapping("/from-country/{fromCountryCode}")
    public List<TariffRule> getTariffRulesByFromCountry(@PathVariable String fromCountryCode) {
        return tariffRuleService.getTariffRulesByFromCountryCode(fromCountryCode);
    }
    
    // New endpoint for to country
    @GetMapping("/to-country/{toCountryCode}")
    public List<TariffRule> getTariffRulesByToCountry(@PathVariable String toCountryCode) {
        return tariffRuleService.getTariffRulesByToCountryCode(toCountryCode);
    }
    
    @GetMapping("/product/{productId}")
    public List<TariffRule> getTariffRulesByProduct(@PathVariable Long productId) {
        return tariffRuleService.getTariffRulesByProductId(productId);
    }
    
    @PostMapping
    public TariffRule createTariffRule(@RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRule(tariffRule);
    }
    
    // Backward compatibility - sets both from and to country to the same country
    @PostMapping("/country/{countryCode}/product/{productId}")
    public TariffRule createTariffRuleWithCountryAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRuleByCountriesAndProduct(fromCountryCode, toCountryCode, productId, tariffRule);
    }
    
    // New endpoint for creating tariff rules with separate from and to countries
    @PostMapping("/from-country/{fromCountryCode}/to-country/{toCountryCode}/product/{productId}")
    public TariffRule createTariffRuleWithCountriesAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRuleByCountriesAndProduct(fromCountryCode, toCountryCode, productId, tariffRule);
    }
    
    @PutMapping("/{id}")
    public TariffRule updateTariffRule(@PathVariable Long id, @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(id, tariffRule);
    }
    
    // Backward compatibility - updates rule associated with country (either from or to)
    @PutMapping("/country/{countryCode}/product/{productId}/{id}")
    public TariffRule updateTariffRuleWithCountryAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(fromCountryCode, toCountryCode, productId, id, tariffRule);
    }
    
    // New endpoint for updating tariff rules with specific from and to countries
    @PutMapping("/from-country/{fromCountryCode}/to-country/{toCountryCode}/product/{productId}/{id}")
    public TariffRule updateTariffRuleWithCountriesAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(fromCountryCode, toCountryCode, productId, id, tariffRule);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTariffRule(@PathVariable Long id) {
        tariffRuleService.deleteTariffRule(id);
        return ResponseEntity.ok().build();
    }
    
    // Backward compatibility - deletes rule associated with country (either from or to)
    @DeleteMapping("/country/{countryCode}/product/{productId}/{id}")
    public ResponseEntity<?> deleteTariffRuleWithCountryAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @PathVariable Long id) {
        tariffRuleService.deleteTariffRule(fromCountryCode, toCountryCode, productId, id);
        return ResponseEntity.ok().build();
    }
    
    // New endpoint for deleting tariff rules with specific from and to countries
    @DeleteMapping("/from-country/{fromCountryCode}/to-country/{toCountryCode}/product/{productId}/{id}")
    public ResponseEntity<?> deleteTariffRuleWithCountriesAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @PathVariable Long id) {
        tariffRuleService.deleteTariffRule(fromCountryCode, toCountryCode, productId, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<TariffRule> getTariffRulesByCriteria(
            @RequestParam(required = false) String fromCountryName,
            @RequestParam(required = false) String toCountryName,
            @RequestParam(required = false) Integer effectiveYear,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Long productId) {
        return tariffRuleService.getTariffRulesByCriteria(fromCountryName, toCountryName, effectiveYear, productName, productId);
    }
}