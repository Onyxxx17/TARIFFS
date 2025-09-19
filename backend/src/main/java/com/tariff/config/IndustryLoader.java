package com.tariff.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.repository.IndustryRepository;

@Component
public class IndustryLoader implements CommandLineRunner{
    @Autowired
    private IndustryRepository industryRepository;

    @Override 
    public void run(String... args) throws Exception {
        // industryRepository.save(new Industry("Agriculture", "plants and stuff"));
        // industryRepository.save(new Industry("Automotives", "vroom vroom"));
        // industryRepository.save(new Industry("Textiles", "asdwf"));
        // industryRepository.save(new Industry("Steel", null));
        // industryRepository.save(new Industry("Electronics", "asdwf"));
        
    }
}
