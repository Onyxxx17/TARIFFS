package com.tariff.config;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tariff.entity.TariffRule;
import com.tariff.repository.TariffRuleRepository;

@Component
public class TariffRuleLoader implements CommandLineRunner{
    @Autowired
    private TariffRuleRepository tariffRuleRepository;

    @Override 
    public void run(String... args) throws Exception {
        LocalDate today = LocalDate.now();
    
    // Quick sample data
    TariffRule rule1 = new TariffRule("China", "USA", "Electronics", 
                                     new BigDecimal("25.0"), today);
                                     
    TariffRule rule2 = new TariffRule("Germany", "USA", "Automotive", 
                                     new BigDecimal("15.5"), today);
                                     
    TariffRule rule3 = new TariffRule("Japan", "USA", "Steel", 
                                     new BigDecimal("30.0"), today);
                                     
    TariffRule rule4 = new TariffRule("Canada", "USA", "Agriculture", 
                                     new BigDecimal("5.25"), today);
                                     
    TariffRule rule5 = new TariffRule("India", "USA", "Textiles", 
                                     new BigDecimal("12.75"), today);

    // Save rules
    tariffRuleRepository.save(rule1);
    tariffRuleRepository.save(rule2);  
    tariffRuleRepository.save(rule3);
    tariffRuleRepository.save(rule4);
    tariffRuleRepository.save(rule5);
    tariffRuleRepository.deleteById(4l);

    }
}
