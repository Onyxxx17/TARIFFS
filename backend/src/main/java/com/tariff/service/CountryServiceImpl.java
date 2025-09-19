package com.tariff.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.Country;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.repository.CountryRepository;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {
    
    private CountryRepository countryRepository;
    
    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    
    @Override
    public List<Country> listCountry() {
        return countryRepository.findAll();
    }
    
    @Override
    public Country getCountry(Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(id));
    }
    
    @Override
    public Country addCountry(Country country) {
        return countryRepository.save(country);
    }
    
    @Override
    public Country updateCountry(Long id, Country country) {
        return countryRepository.findById(id).map(existingCountry -> {
            existingCountry.setName(country.getName());
            existingCountry.setIsoCode(country.getIsoCode());   
            existingCountry.setCurrency(country.getCurrency()); 
            return countryRepository.save(existingCountry);
        }).orElseThrow(() -> new CountryNotFoundException(id));
    }
    
    @Override
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new CountryNotFoundException(id);
        }
        countryRepository.deleteById(id);
    }
}