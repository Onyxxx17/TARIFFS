package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.entity.Country;
import com.tariff.repository.CountryRepository;

@Component
public class CountryLoader implements CommandLineRunner {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only load data if the table is empty
        if (countryRepository.count() == 0) {
            countryRepository.save(new Country("Singapore", "SGP", "SGD"));
            countryRepository.save(new Country("Canada", "CAN", "CAD"));
            countryRepository.save(new Country("United Kingdom", "GBR", "GBP"));
            countryRepository.save(new Country("USA", "USA", "USD"));
            System.out.println("Sample countries loaded successfully!");
        } else {
            System.out.println("Countries already exist, skipping data loading.");
        }
    }
}
