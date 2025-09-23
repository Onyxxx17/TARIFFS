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
    public Country getCountry(String countryCode) {
        return countryRepository.findById(countryCode)
                .orElseThrow(() -> new CountryNotFoundException(countryCode));
    }

    @Override
    public Optional<Country> getCountryByName(String name) {
        return countryRepository.findByName(name);
    }

    @Override
    public Country addCountry(Country country) {
        // Check for duplicates by name
        if (countryRepository.existsByName(country.getName())) {
            throw new DuplicateCountryException(
                    String.format("Country with name '%s' already exists", country.getName())
            );
        }
        return countryRepository.save(country);
    }

    @Override
    public Country updateCountry(String countryCode, Country country) {
        if (!countryRepository.existsById(countryCode)) {
            throw new CountryNotFoundException(countryCode);
        }

        // Check for duplicates but exclude the current country being updated
        Optional<Country> existingByName = countryRepository.findByName(country.getName());
        if (existingByName.isPresent() && !existingByName.get().getCountryCode().equals(countryCode)) {
            throw new DuplicateCountryException(
                    String.format("Country with name '%s' already exists", country.getName())
            );
        }

        country.setCountryCode(countryCode);
        return countryRepository.save(country);
    }

    @Override
    public void deleteCountry(String countryCode) {
        if (!countryRepository.existsById(countryCode)) {
            throw new CountryNotFoundException(countryCode);
        }
        countryRepository.deleteById(countryCode);
    }

    @Override
    public boolean existsByName(String name) {
        return countryRepository.existsByName(name);
    }


}
