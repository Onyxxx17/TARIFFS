package com.tariff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tariff.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    
    // Find by name
    Optional<Country> findByName(String name);
    
    // Find by ISO code
    Optional<Country> findByIsoCode(String isoCode);
    
    // Find by currency
    List<Country> findByCurrency(String currency);
    
    // Check if country name already exists
    boolean existsByName(String name);
    
    // Check if ISO code already exists
    boolean existsByIsoCode(String isoCode);
    
    // Check if either name or ISO code exists (for duplicate prevention)
    boolean existsByNameOrIsoCode(String name, String isoCode);
}
