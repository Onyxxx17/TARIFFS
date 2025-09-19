package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.repository.CountryRepository;

@Component
public class CountryLoader implements CommandLineRunner{
    @Autowired
    private CountryRepository countryRepository;

    @Override 
    public void run(String... args) throws Exception {
        // countryRepository.save(new Country("Singapore", "SGD", "SGP"));
        // countryRepository.save(new Country("Canada", "CAN", "CAD"));
        // countryRepository.save(new Country("United Kingdom", "GBR", "GBP"));
        // countryRepository.save(new Country("USA", "USA", "USD"));

    }
}
