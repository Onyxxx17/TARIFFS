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

import com.tariff.entity.TariffRule;
import com.tariff.repository.TariffRuleRepository;


@RestController
@RequestMapping("api/tariffs")
public class TariffRuleController {
    @Autowired
    private TariffRuleRepository tariffRuleRepository;

   // GET all countries
    @GetMapping
    public List<TariffRule> getAllTariffRules() {
        return tariffRuleRepository.findAll();
    }

    // GET TariffRule by ID
    @GetMapping("/{id}")
    public TariffRule getTariffRuleById(@PathVariable Long id) {
        return tariffRuleRepository.findById(id).orElse(null);
    }

    // POST create new country
    @PostMapping
    public TariffRule createTariffRule(@RequestBody TariffRule tariffRule) {
        return tariffRuleRepository.save(tariffRule);
    }

    // PUT update country
    @PutMapping("/{id}")
    public TariffRule updateTariffRule(@PathVariable Long id, @RequestBody TariffRule tariffRule) {
        tariffRule.setId(id);
        return tariffRuleRepository.save(tariffRule);
    }

    // DELETE country
    @DeleteMapping("/{id}")
    public void deleteTariffRule(@PathVariable Long id) {
        tariffRuleRepository.deleteById(id);
    }
}
