package com.tariff.controller;

import com.tariff.entity.Country;
import com.tariff.service.CountryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@Tag(name = "Countries", description = "Country management endpoints")
public class CountryController {

    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(summary = "Get all countries", description = "Returns a list of all countries")
    public List<Country> getAllCountries() {
        return countryService.listCountry();
    }

    @GetMapping("/{countryCode}")
    @Operation(summary = "Get country by code", description = "Returns a specific country by its country code")
    public Country getCountryByCode(
            @Parameter(description = "Country code", example = "C056")
            @PathVariable String countryCode
    ) {
        return countryService.getCountry(countryCode);
    }

    @GetMapping("/search/by-name")
    @Operation(summary = "Get country by name", description = "Searches for and returns a country by its name")
    public ResponseEntity<?> getCountryByName(@RequestParam String name) {
        try {
            var country = countryService.getCountryByName(name);
            if (country.isPresent()) {
                Country c = country.get();
                return ResponseEntity.ok(java.util.Map.of(
                        "countryCode", c.getCountryCode(),
                        "countryName", c.getName()
                ));
            }
            return ResponseEntity.status(404).body(java.util.Map.of("error", "Country not found: " + name));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(java.util.Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create a new country (Admin only)", description = "Creates a new country - requires ADMIN role")
    public Country createCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @PutMapping("/{countryCode}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update a country (Admin only)", description = "Updates an existing country - requires ADMIN role")
    public Country updateCountry(@PathVariable String countryCode, @RequestBody Country country) {
        return countryService.updateCountry(countryCode, country);
    }

    @DeleteMapping("/{countryCode}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a country (Admin only)", description = "Deletes a country by its country code - requires ADMIN role")
    public ResponseEntity<?> deleteCountry(@PathVariable String countryCode) {
        countryService.deleteCountry(countryCode);
        return ResponseEntity.ok().build();
    }
}
