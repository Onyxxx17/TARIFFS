package com.tariff.service;

import com.tariff.entity.Country;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.exception.DuplicateCountryException;
import com.tariff.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

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
    public Optional<Country> getCountryByName(String name) {
        return countryRepository.findByName(name);
    }

    @Override
    public Optional<Country> getCountryByIsoCode(String isoCode) {
        return countryRepository.findByIsoCode(isoCode);
    }

    @Override
    public Country addCountry(Country country) {
        // Check for duplicates by name or ISO code
        if (countryRepository.existsByNameOrIsoCode(country.getName(), country.getIsoCode())) {
            if (countryRepository.existsByName(country.getName())) {
                throw new DuplicateCountryException(
                    String.format("Country with name '%s' already exists", country.getName())
                );
            }
            if (countryRepository.existsByIsoCode(country.getIsoCode())) {
                throw new DuplicateCountryException(
                    String.format("Country with ISO code '%s' already exists", country.getIsoCode())
                );
            }
        }
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(Long id, Country country) {
        if (!countryRepository.existsById(id)) {
            throw new CountryNotFoundException(id);
        }
        
        // Check for duplicates but exclude the current country being updated
        Optional<Country> existingByName = countryRepository.findByName(country.getName());
        if (existingByName.isPresent() && !existingByName.get().getId().equals(id)) {
            throw new DuplicateCountryException(
                String.format("Country with name '%s' already exists", country.getName())
            );
        }
        
        Optional<Country> existingByIsoCode = countryRepository.findByIsoCode(country.getIsoCode());
        if (existingByIsoCode.isPresent() && !existingByIsoCode.get().getId().equals(id)) {
            throw new DuplicateCountryException(
                String.format("Country with ISO code '%s' already exists", country.getIsoCode())
            );
        }
        
        country.setId(id);
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new CountryNotFoundException(id);
        }
        countryRepository.deleteById(id);
    }
    
    @Override
    public boolean existsByName(String name) {
        return countryRepository.existsByName(name);
    }
    
    @Override
    public boolean existsByIsoCode(String isoCode) {
        return countryRepository.existsByIsoCode(isoCode);
    }
    
    @Override
    public boolean existsByNameOrIsoCode(String name, String isoCode) {
        return countryRepository.existsByNameOrIsoCode(name, isoCode);
    }
}