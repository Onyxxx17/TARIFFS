package com.tariff.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.entity.Country;
import com.tariff.entity.Industry;
import com.tariff.entity.TariffRule;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.IndustryRepository;
import com.tariff.repository.TariffRuleRepository;

// @Component
public class TariffRuleLoader implements CommandLineRunner {

    @Autowired
    private TariffRuleRepository tariffRuleRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Override
    public void run(String... args) throws Exception {
        //     LocalDate today = LocalDate.now();

        //     Industry electronics = new Industry("Electronics & Technology", "Challenger");
        //     Industry textiles = new Industry("Textiles & Apparel", "Chanel");
        //     Industry automotive = new Industry("Automotive", "Tesla");
        //     Country singapore = new Country("Singapore", "SGD", "SGP");
        //     Country canada = new Country("Canada", "CAN", "CAD");
        //     Country uk = new Country("United Kingdom", "GBR", "GBP");
        //     Country usa = new Country("USA", "USA", "USD");
        //     electronics = industryRepository.save(electronics);
        //     textiles = industryRepository.save(textiles);
        //     automotive = industryRepository.save(automotive);
        //     singapore = countryRepository.save(singapore);
        //     canada = countryRepository.save(canada);
        //     uk = countryRepository.save(uk);
        //     usa = countryRepository.save(usa);
        // // Country singapore = countryRepository.findByName("Singapore")
        // //     .orElseThrow(() -> new RuntimeException("Country not found: Singapore"));  
        // // Country canada = countryRepository.findByName("Canada")
        // //     .orElseThrow(() -> new RuntimeException("Country not found: Singapore")); 
        // // Country uk = countryRepository.findByName("United Kingdom")
        // //     .orElseThrow(() -> new RuntimeException("Country not found: United Kingdom"));   
        // // Country usa = countryRepository.findByName("USA")
        // //     .orElseThrow(() -> new RuntimeException("Country not found: USA")); 
        // // Quick sample data
        // TariffRule rule1 = new TariffRule(singapore, usa, electronics, 
        //                                  new BigDecimal("25.0"), today);
        // TariffRule rule2 = new TariffRule(canada, usa, automotive, 
        //                                  new BigDecimal("15.5"), today);
        // TariffRule rule3 = new TariffRule(uk, usa, textiles, 
        //                                  new BigDecimal("30.0"), today);
        // // Save rules
        // tariffRuleRepository.save(rule1);
        // tariffRuleRepository.save(rule2);  
        // tariffRuleRepository.save(rule3);
    }
}
