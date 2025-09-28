package com.tariff.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tariff.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    
    Optional<Category> findByName(String name);
}
