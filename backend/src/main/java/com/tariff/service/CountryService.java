package com.tariff.service;

import java.util.List;
import java.util.Optional;

import com.tariff.entity.Country;

public interface CountryService {

    List<Country> listCountry();

    Country getCountry(Long id);

    Optional<Country> getCountryByName(String name);

    Optional<Country> getCountryByIsoCode(String isoCode);

    Country addCountry(Country country);

    Country updateCountry(Long id, Country country);

    void deleteCountry(Long id);

    boolean existsByName(String name);

    boolean existsByIsoCode(String isoCode);

    boolean existsByNameOrIsoCode(String name, String isoCode);
}
