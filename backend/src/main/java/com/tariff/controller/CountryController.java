package com.tariff.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tariff.entity.Country;
import com.tariff.repository.CountryRepository;


@RestController
@RequestMapping("api/countries")
public class CountryController {
    @Autowired
    private CountryRepository countryRepository;

   // GET all countries
    @GetMapping
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    // GET country by ID
    @GetMapping("/{id}")
    public Country getCountryById(@PathVariable Long id) {
        return countryRepository.findById(id).orElse(null);
    }

    // POST create new country
    @PostMapping
    public Country createCountry(@RequestBody Country country) {
        return countryRepository.save(country);
    }

    // PUT update country
    @PutMapping("/{id}")
    public Country updateCountry(@PathVariable Long id, @RequestBody Country country) {
        country.setId(id);
        return countryRepository.save(country);
    }

    // DELETE country
    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable Long id) {
        countryRepository.deleteById(id);
    }
}
