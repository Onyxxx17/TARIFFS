package com.tariff.controller;

import com.tariff.entity.Country;
import com.tariff.service.CountryService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@SecurityRequirement(name = "Bearer Authentication")
public class CountryController {

    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.listCountry();
    }

    @GetMapping("/{countryCode}")
    public Country getCountryByCode(
            @Parameter(description = "Country code", example = "C056")
            @PathVariable String countryCode
    ) {

        return countryService.getCountry(countryCode);
    }

    @GetMapping("/search/by-name")
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
    public Country createCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }

    @PutMapping("/{countryCode}")
    public Country updateCountry(@PathVariable String countryCode, @RequestBody Country country) {
        return countryService.updateCountry(countryCode, country);
    }

    @DeleteMapping("/{countryCode}")
    public ResponseEntity<?> deleteCountry(@PathVariable String countryCode) {
        countryService.deleteCountry(countryCode);
        return ResponseEntity.ok().build();
    }
}
