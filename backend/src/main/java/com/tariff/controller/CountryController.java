package com.tariff.controller;

import com.tariff.entity.Country;
import com.tariff.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    
    private CountryService countryService;
    
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }
    
    @GetMapping
    public List<Country> getAllCountries() {
        return countryService.listCountry();
    }
    
    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id) {
        return countryService.getCountry(id);
    }
    
    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        return countryService.addCountry(country);
    }
    
    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable Long id, @RequestBody Country country) {
        return countryService.updateCountry(id, country);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable Long id) {
        countryService.deleteCountry(id);
        return ResponseEntity.ok().build();
    }
}