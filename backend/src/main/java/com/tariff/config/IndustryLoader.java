package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.entity.Industry;
import com.tariff.repository.IndustryRepository;

@Component
public class IndustryLoader implements CommandLineRunner {

    @Autowired
    private IndustryRepository industryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only load data if the table is empty
        if (industryRepository.count() == 0) {
            industryRepository.save(new Industry("Agriculture", "Plants and agricultural products"));
            industryRepository.save(new Industry("Automotives", "Motor vehicles and automotive parts"));
            industryRepository.save(new Industry("Textiles", "Clothing, fabrics and textile products"));
            industryRepository.save(new Industry("Steel", "Iron and steel products"));
            industryRepository.save(new Industry("Electronics", "Electronic devices and components"));
            System.out.println("Sample industries loaded successfully!");
        } else {
            System.out.println("Industries already exist, skipping data loading.");
        }
    }
}
