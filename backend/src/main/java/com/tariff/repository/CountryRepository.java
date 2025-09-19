package com.tariff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
    List<Country> findByCurrency(String currency);
}
