package com.tariff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCountryId(Long countryId);
    List<Product> findByIndustryId(Long industryId);
    Optional<Product> findByIdAndCountryId(Long id, Long countryId);
    Optional<Product> findByIdAndIndustryId(Long id, Long industryId);
    List<Product> findByHsCode(String hsCode);
    List<Product> findByCountryIdAndIndustryId(Long countryId, Long industryId);
}