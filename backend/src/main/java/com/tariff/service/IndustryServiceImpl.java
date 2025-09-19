package com.tariff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.Industry;
import com.tariff.exception.IndustryNotFoundException;
import com.tariff.repository.IndustryRepository;

@Service
@Transactional
public class IndustryServiceImpl implements IndustryService {
    
    private IndustryRepository industryRepository;
    
    public IndustryServiceImpl(IndustryRepository industryRepository) {
        this.industryRepository = industryRepository;
    }
    
    @Override
    public List<Industry> listIndustry() {
        return industryRepository.findAll();
    }
    
    @Override
    public Industry getIndustry(Long id) {
        return industryRepository.findById(id)
                .orElseThrow(() -> new IndustryNotFoundException(id));
    }
    
    @Override
    public Industry addIndustry(Industry industry) {
        return industryRepository.save(industry);
    }
    
    @Override
    public Industry updateIndustry(Long id, Industry industry) {
        return industryRepository.findById(id).map(existingIndustry -> {
            existingIndustry.setName(industry.getName());
            return industryRepository.save(existingIndustry);
        }).orElseThrow(() -> new IndustryNotFoundException(id));
    }
    
    @Override
    public void deleteIndustry(Long id) {
        if (!industryRepository.existsById(id)) {
            throw new IndustryNotFoundException(id);
        }
        industryRepository.deleteById(id);
    }
}