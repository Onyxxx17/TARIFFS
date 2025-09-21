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
    @GetMapping("/country/{countryId}")
    public List<TariffRule> getTariffRulesByCountry(@PathVariable Long countryId) {
        return tariffRuleService.getTariffRulesByCountryId(countryId);
    }
    
    // New endpoint for from country
    @GetMapping("/from-country/{fromCountryId}")
    public List<TariffRule> getTariffRulesByFromCountry(@PathVariable Long fromCountryId) {
        return tariffRuleService.getTariffRulesByFromCountryId(fromCountryId);
    }
    
    // New endpoint for to country
    @GetMapping("/to-country/{toCountryId}")
    public List<TariffRule> getTariffRulesByToCountry(@PathVariable Long toCountryId) {
        return tariffRuleService.getTariffRulesByToCountryId(toCountryId);
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
    @PostMapping("/country/{countryId}/product/{productId}")
    public TariffRule createTariffRuleWithCountryAndProduct(
            @PathVariable Long fromCountryId,
            @PathVariable Long toCountryId,
            @PathVariable Long productId,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRuleByCountriesAndProduct(fromCountryId, toCountryId, productId, tariffRule);
    }
    
    // New endpoint for creating tariff rules with separate from and to countries
    @PostMapping("/from-country/{fromCountryId}/to-country/{toCountryId}/product/{productId}")
    public TariffRule createTariffRuleWithCountriesAndProduct(
            @PathVariable Long fromCountryId,
            @PathVariable Long toCountryId,
            @PathVariable Long productId,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRuleByCountriesAndProduct(fromCountryId, toCountryId, productId, tariffRule);
    }
    
    @PutMapping("/{id}")
    public TariffRule updateTariffRule(@PathVariable Long id, @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(id, tariffRule);
    }
    
    // Backward compatibility - updates rule associated with country (either from or to)
    @PutMapping("/country/{countryId}/product/{productId}/{id}")
    public TariffRule updateTariffRuleWithCountryAndProduct(
            @PathVariable Long fromCountryId,
            @PathVariable Long toCountryId,
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(fromCountryId, toCountryId, productId, id, tariffRule);
    }
    
    // New endpoint for updating tariff rules with specific from and to countries
    @PutMapping("/from-country/{fromCountryId}/to-country/{toCountryId}/product/{productId}/{id}")
    public TariffRule updateTariffRuleWithCountriesAndProduct(
            @PathVariable Long fromCountryId,
            @PathVariable Long toCountryId,
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(fromCountryId, toCountryId, productId, id, tariffRule);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTariffRule(@PathVariable Long id) {
        tariffRuleService.deleteTariffRule(id);
        return ResponseEntity.ok().build();
    }
    
    // Backward compatibility - deletes rule associated with country (either from or to)
    @DeleteMapping("/country/{countryId}/product/{productId}/{id}")
    public ResponseEntity<?> deleteTariffRuleWithCountryAndProduct(
            @PathVariable Long fromCountryId,
            @PathVariable Long toCountryId,
            @PathVariable Long productId,
            @PathVariable Long id) {
        tariffRuleService.deleteTariffRule(fromCountryId, toCountryId, productId, id);
        return ResponseEntity.ok().build();
    }
    
    // New endpoint for deleting tariff rules with specific from and to countries
    @DeleteMapping("/from-country/{fromCountryId}/to-country/{toCountryId}/product/{productId}/{id}")
    public ResponseEntity<?> deleteTariffRuleWithCountriesAndProduct(
            @PathVariable Long fromCountryId,
            @PathVariable Long toCountryId,
            @PathVariable Long productId,
            @PathVariable Long id) {
        tariffRuleService.deleteTariffRule(fromCountryId, toCountryId, productId, id);
        return ResponseEntity.ok().build();
    }
}