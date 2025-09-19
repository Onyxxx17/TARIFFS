package com.tariff.service;

import java.util.List;

import com.tariff.entity.Country;

public interface CountryService {
    List<Country> listCountry();
    Country getCountry(Long id);
    Country addCountry(Country country);
    Country updateCountry(Long id, Country country);
    void deleteCountry(Long id);
}
