package com.tariff.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.entity.Country;
import com.tariff.entity.Industry;
import com.tariff.entity.TariffRule;
import com.tariff.repository.TariffRuleRepository;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.IndustryRepository;

@Component
public class TariffRuleLoader implements CommandLineRunner{
    @Autowired
    private TariffRuleRepository tariffRuleRepository;

    @Autowired
    private IndustryRepository industryRepository;
    
    @Autowired
    private CountryRepository countryRepository;


    @Override 
    public void run(String... args) throws Exception {
        LocalDate today = LocalDate.now();
    Industry electronics = industryRepository.findByName("Electronics")
        .orElseThrow(() -> new RuntimeException("Industry not found: Electronics"));

    Industry automotive = industryRepository.findByName("Automotives")
        .orElseThrow(() -> new RuntimeException("Industry not found: Automotives"));

    Industry steel = industryRepository.findByName("Steel")
        .orElseThrow(() -> new RuntimeException("Industry not found: Steel"));       

    Country singapore = countryRepository.findByName("Singapore")
        .orElseThrow(() -> new RuntimeException("Country not found: Singapore"));  

    Country canada = countryRepository.findByName("Canada")
        .orElseThrow(() -> new RuntimeException("Country not found: Singapore")); 

    Country uk = countryRepository.findByName("United Kingdom")
        .orElseThrow(() -> new RuntimeException("Country not found: United Kingdom"));   
    
    Country usa = countryRepository.findByName("USA")
        .orElseThrow(() -> new RuntimeException("Country not found: USA")); 
    
    // Quick sample data
    TariffRule rule1 = new TariffRule(singapore, usa, electronics, 
                                     new BigDecimal("25.0"), today);
                                     
    TariffRule rule2 = new TariffRule(canada, usa, automotive, 
                                     new BigDecimal("15.5"), today);
                                     
    TariffRule rule3 = new TariffRule(uk, usa, steel, 
                                     new BigDecimal("30.0"), today);
                                     

    // Save rules
    tariffRuleRepository.save(rule1);
    tariffRuleRepository.save(rule2);  
    tariffRuleRepository.save(rule3);

    }
}
