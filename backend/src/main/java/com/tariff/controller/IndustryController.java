package com.tariff.controller;

import com.tariff.entity.Industry;
import com.tariff.service.IndustryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/industries")
public class IndustryController {
    
    private IndustryService industryService;
    
    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }
    
    @GetMapping
    public List<Industry> getAllIndustries() {
        return industryService.listIndustry();
    }
    
    @GetMapping("/{id}")
    public Industry getIndustryById(@PathVariable Long id) {
        return industryService.getIndustry(id);
    }
    
    @PostMapping
    public Industry createIndustry(@RequestBody Industry industry) {
        return industryService.addIndustry(industry);
    }
    
    @PutMapping("/{id}")
    public Industry updateIndustry(@PathVariable Long id, @RequestBody Industry industry) {
        return industryService.updateIndustry(id, industry);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIndustry(@PathVariable Long id) {
        industryService.deleteIndustry(id);
        return ResponseEntity.ok().build();
    }
}