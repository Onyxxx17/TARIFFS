package com.tariff.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tariff.entity.Industry;

@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long>{
    
    Optional<Industry> findByName(String name);
}
