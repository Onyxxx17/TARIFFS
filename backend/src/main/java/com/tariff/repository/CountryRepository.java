package com.tariff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tariff.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, String> {

    // Find by name
    Optional<Country> findByName(String name);

    // Check if country name already exists
    boolean existsByName(String name);

    // Find country by name (returns the full Country object with code)
    @Query("SELECT c FROM Country c WHERE c.name = :name")
    Optional<Country> findCountryByName(@Param("name") String name);
}
