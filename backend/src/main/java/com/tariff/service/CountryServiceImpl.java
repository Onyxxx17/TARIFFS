package com.tariff.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.tariff.entity.Country;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.ImportRecordRepository;
import com.tariff.exception.*;

public class CountryServiceImpl implements CountryService {

    private CountryRepository countryRepository;
    private ImportRecordRepository importRecordRepository;

    @Override
    public List<Country> listCountry() {
        return countryRepository.findAll();
    }


    @Override
    public Country getCountry(Long id) {
        return countryRepository.findById(id).map(country -> {
            return country;
        }).orElseThrow(() -> new CountryNotFoundException(id));
    }

    public List<Country> getAllBooksByImportRecordId(@PathVariable (value = "countryId") Long importRecordId) {
        if(!importRecordRepository.existsById(importRecordId)) {
            throw new ImportRecordNotFoundException(importRecordId);
        }
        return countryRepository.findByImportRecordId(importRecordId);
    }

    @Override
    public Country addCountry(Country country) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCountry'");
    }

    @Override
    public Country updateCountry(Long id, Country country) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCountry'");
    }

    @Override
    public void deleteCountry(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCountry'");
    }



    
}
