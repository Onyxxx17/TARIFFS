package com.tariff.controller;

import com.tariff.entity.TariffRule;
import com.tariff.service.TariffRuleService;

import io.swagger.v3.oas.annotations.Parameter;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public Page<TariffRule> getAllTariffRules(@ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return tariffRuleService.listTariffRule(pageable);
    }
    
    @GetMapping("/{id}")
    public TariffRule getTariffRuleById(
        @Parameter(description = "ID of tariff rule", example = "2")
        @PathVariable Long id,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return tariffRuleService.getTariffRule(id);
    }
    
    // Backward compatibility - returns rules where country is either from or to
    @GetMapping("/country/{countryCode}")
    public Page<TariffRule> getTariffRulesByCountry(
        @Parameter(description = "Country Code", example = "C840")
        @PathVariable String countryCode,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable
        ) {
        return tariffRuleService.getTariffRulesByCountryCode(countryCode, pageable);
    }
    
    // New endpoint for from country
    @GetMapping("/from-country/{fromCountryCode}")
    public Page<TariffRule> getTariffRulesByFromCountry(
        @Parameter(description = "Country Code", example = "C840")
        @PathVariable String fromCountryCode,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return tariffRuleService.getTariffRulesByFromCountryCode(fromCountryCode, pageable);
    }
    
    // New endpoint for to country
    @GetMapping("/to-country/{toCountryCode}")
    public Page<TariffRule> getTariffRulesByToCountry(
        @Parameter(description = "Country Code", example = "C840")
        @PathVariable String toCountryCode,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return tariffRuleService.getTariffRulesByToCountryCode(toCountryCode, pageable);
    }
    
    @GetMapping("/product/{productId}")
    public Page<TariffRule> getTariffRulesByProduct(
        @Parameter(description = "Product ID", example = "10129")
        @PathVariable Long productId,
        @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return tariffRuleService.getTariffRulesByProductId(productId, pageable);
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
}