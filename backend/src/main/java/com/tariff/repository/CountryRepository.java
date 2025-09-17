package com.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.Country;


public interface CountryRepository extends JpaRepository<Country, Long> {
    
}
