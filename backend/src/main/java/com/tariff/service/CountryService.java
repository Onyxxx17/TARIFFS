package com.tariff.service;

import java.util.List;
import java.util.Optional;

import com.tariff.entity.Country;

public interface CountryService {

    List<Country> listCountry();

    Country getCountry(String countryCode);

    Optional<Country> getCountryByName(String name);

    Country addCountry(Country country);

    Country updateCountry(String countryCode, Country country);

    void deleteCountry(String countryCode);

    boolean existsByName(String name);
}
