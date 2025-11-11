package com.tariff.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tariff.dto.PageResponse;
import com.tariff.dto.response.TariffComparisonDTO;
import com.tariff.dto.response.TariffRateOverTimeDTO;
import com.tariff.entity.TariffRule;
import com.tariff.service.TariffRuleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tariff-rules")
@Tag(name = "Tariff Rules", description = "Tariff rule management endpoints")
public class TariffRuleController {

    private TariffRuleService tariffRuleService;

    public TariffRuleController(TariffRuleService tariffRuleService) {
        this.tariffRuleService = tariffRuleService;
    }

    @GetMapping
    @Operation(summary = "Get all tariff rules (paginated)", description = "Returns a paginated list of tariff rules")
    public ResponseEntity<PageResponse<TariffRule>> getAllTariffRules(
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        var page = tariffRuleService.listTariffRule(pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tariff rule by ID", description = "Returns a specific tariff rule by its ID")
    public TariffRule getTariffRuleById(
            @Parameter(description = "ID of tariff rule", example = "2")
            @PathVariable Long id,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        return tariffRuleService.getTariffRule(id);
    }

    // Backward compatibility - returns rules where country is either from or to
    @GetMapping("/country/{countryCode}")
    @Operation(summary = "Get tariff rules by country code", description = "Returns tariff rules where the country is either the exporting or importing country")
    public ResponseEntity<PageResponse<TariffRule>> getTariffRulesByCountry(
            @Parameter(description = "Country Code", example = "C840")
            @PathVariable String countryCode,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable
    ) {
        var page = tariffRuleService.getTariffRulesByCountryCode(countryCode, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }

    // New endpoint for to country
    // @GetMapping("/to-country/{toCountryCode}")
    // public ResponseEntity<PageResponse<TariffRule>> getTariffRulesByToCountry(
    //         @PathVariable("toCountryCode") String toCountryCode,
    //         @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
    //     var page = tariffRuleService.getTariffRulesByToCountryCode(toCountryCode, pageable);
    //     return ResponseEntity.ok(new PageResponse<>(page));
    // }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get tariff rules by product ID", description = "Returns tariff rules for a specific product")
    public ResponseEntity<PageResponse<TariffRule>> getTariffRulesByProduct(
            @PathVariable Long productId,
            @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
        var page = tariffRuleService.getTariffRulesByProductId(productId, pageable);
        return ResponseEntity.ok(new PageResponse<>(page));
    }


    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new tariff rule (Admin only)", description = "Creates a new tariff rule - requires ADMIN role")
    public TariffRule createTariffRule(@RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRule(tariffRule);
    }
    

    // Backward compatibility - sets both from and to country to the same country
    // @PostMapping("/country/{countryCode}/product/{productId}")
    // public TariffRule createTariffRuleWithCountryAndProduct(
    //         @PathVariable("countryCode") String countryCode,
    //         @PathVariable("productId") Long productId,
    //         @RequestBody TariffRule tariffRule) {
    //     // backward-compatible: same country for from and to
    //     return tariffRuleService.addTariffRuleByCountriesAndProduct(countryCode, countryCode, productId, tariffRule);
    // }

    // New endpoint for creating tariff rules with separate from and to countries
    @PostMapping("/from-country/{fromCountryCode}/to-country/{toCountryCode}/product/{productId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create tariff rule with specific countries (Admin only)", description = "Creates a tariff rule for specific from/to countries and product - requires ADMIN role")
    public TariffRule createTariffRuleWithCountriesAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.addTariffRuleByCountriesAndProduct(fromCountryCode, toCountryCode, productId, tariffRule);
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update a tariff rule (Admin only)", description = "Updates an existing tariff rule - requires ADMIN role")
    public TariffRule updateTariffRule(@PathVariable Long id, @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(id, tariffRule);
    }

    // Backward compatibility - updates rule associated with country (either from or to)
    // @PutMapping("/country/{countryCode}/product/{productId}/{id}")
    // public TariffRule updateTariffRuleWithCountryAndProduct(
    //         @PathVariable("countryCode") String countryCode,
    //         @PathVariable("productId") Long productId,
    //         @PathVariable("id") Long id,
    //         @RequestBody TariffRule tariffRule) {
    //     // backward-compatible: same country for from and to
    //     return tariffRuleService.updateTariffRule(countryCode, countryCode, productId, id, tariffRule);
    // }

    // New endpoint for updating tariff rules with specific from and to countries
    @PutMapping("/from-country/{fromCountryCode}/to-country/{toCountryCode}/product/{productId}/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update tariff rule with specific countries (Admin only)", description = "Updates a tariff rule for specific from/to countries and product - requires ADMIN role")
    public TariffRule updateTariffRuleWithCountriesAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @PathVariable Long id,
            @RequestBody TariffRule tariffRule) {
        return tariffRuleService.updateTariffRule(fromCountryCode, toCountryCode, productId, id, tariffRule);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a tariff rule (Admin only)", description = "Deletes a tariff rule by ID - requires ADMIN role")
    public ResponseEntity<?> deleteTariffRule(@PathVariable Long id) {
        tariffRuleService.deleteTariffRule(id);
        return ResponseEntity.ok().build();
    }

    // Backward compatibility - deletes rule associated with country (either from or to)
    // @DeleteMapping("/country/{countryCode}/product/{productId}/{id}")
    // public ResponseEntity<?> deleteTariffRuleWithCountryAndProduct(
    //         @PathVariable("countryCode") String countryCode,
    //         @PathVariable("productId") Long productId,
    //         @PathVariable("id") Long id) {
    //     // backward-compatible: same country for from and to
    //     tariffRuleService.deleteTariffRule(countryCode, countryCode, productId, id);
    //     return ResponseEntity.ok().build();
    // }

    // New endpoint for deleting tariff rules with specific from and to countries
    @DeleteMapping("/from-country/{fromCountryCode}/to-country/{toCountryCode}/product/{productId}/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete tariff rule with specific countries (Admin only)", description = "Deletes a tariff rule for specific from/to countries and product - requires ADMIN role")
    public ResponseEntity<?> deleteTariffRuleWithCountriesAndProduct(
            @PathVariable String fromCountryCode,
            @PathVariable String toCountryCode,
            @PathVariable Long productId,
            @PathVariable Long id) {
        tariffRuleService.deleteTariffRule(fromCountryCode, toCountryCode, productId, id);
        return ResponseEntity.ok().build();
    }

    // New endpoint for getting tariff rates over time
    @GetMapping("/rates-over-time")
    @Operation(summary = "Get tariff rates over time", description = "Returns tariff rates for all years for given from/to countries and product")
    public ResponseEntity<List<TariffRateOverTimeDTO>> getTariffRatesOverTime(
            @RequestParam String fromCountryCode,
            @RequestParam String toCountryCode,
            @RequestParam Long productId) {
        List<TariffRateOverTimeDTO> rates = tariffRuleService.getTariffRatesOverTime(fromCountryCode, toCountryCode, productId);
        return ResponseEntity.ok(rates);
    }

    // New endpoint for comparing tariff rates between two countries
    @GetMapping("/compare-tariffs")
    @Operation(summary = "Compare tariff rates between countries", description = "Compares tariff rates between two countries for a specific product")
    public ResponseEntity<TariffComparisonDTO> compareTariffRates(
            @RequestParam String country1Code,
            @RequestParam String country2Code,
            @RequestParam Long productId) {
        TariffComparisonDTO comparison = tariffRuleService.compareTariffRates(country1Code, country2Code, productId);
        return ResponseEntity.ok(comparison);
    }

    // New endpoint for getting all tariff rules by from-country, to-country, and product (all years)
    @GetMapping("/by-countries-and-product")
    @Operation(summary = "Get all tariff rules by countries and product", description = "Returns all tariff rules for given from/to countries and product across all years")
    public ResponseEntity<List<TariffRule>> getTariffRulesByCountriesAndProduct(
            @RequestParam String fromCountryCode,
            @RequestParam String toCountryCode,
            @RequestParam Long productId) {
        List<TariffRule> rules = tariffRuleService.getTariffRulesByCountriesAndProduct(fromCountryCode, toCountryCode, productId);
        return ResponseEntity.ok(rules);
    }

    // New endpoint for getting a specific tariff rule by countries, product, and year
    @GetMapping("/by-countries-product-year")
    @Operation(summary = "Get tariff rule by countries, product, and year", description = "Returns a specific tariff rule for given from/to countries, product, and effective year")
    public ResponseEntity<TariffRule> getTariffRuleByCountriesProductAndYear(
            @RequestParam String fromCountryCode,
            @RequestParam String toCountryCode,
            @RequestParam Long productId,
            @RequestParam Integer year) {
        TariffRule rule = tariffRuleService.getTariffRuleByCountriesProductAndYear(fromCountryCode, toCountryCode, productId, year);
        return ResponseEntity.ok(rule);
    }
}
