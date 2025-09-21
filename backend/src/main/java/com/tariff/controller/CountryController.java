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
    
    @GetMapping("/{countryCode}")
    public Country getCountryByCode(@PathVariable String countryCode) {
        return countryService.getCountry(countryCode);
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