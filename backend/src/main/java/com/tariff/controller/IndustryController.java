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

import com.tariff.entity.Industry;
import com.tariff.repository.IndustryRepository;


@RestController
@RequestMapping("api/industry")
public class IndustryController {
    @Autowired
    private IndustryRepository industryRepository;

   // GET all industries
    @GetMapping
    public List<Industry> getAllIndustries() {
        return industryRepository.findAll();
    }

    // GET Industry by ID
    @GetMapping("/{id}")
    public Industry getIndustryById(@PathVariable Long id) {
        return industryRepository.findById(id).orElse(null);
    }

    // POST create new Industry
    @PostMapping
    public Industry createIndustry(@RequestBody Industry Industry) {
        return industryRepository.save(Industry);
    }

    // PUT update Industry
    @PutMapping("/{id}")
    public Industry updateIndustry(@PathVariable Long id, @RequestBody Industry Industry) {
        Industry.setId(id);
        return industryRepository.save(Industry);
    }

    // DELETE Industry
    @DeleteMapping("/{id}")
    public void deleteIndustry(@PathVariable Long id) {
        industryRepository.deleteById(id);
    }
}
