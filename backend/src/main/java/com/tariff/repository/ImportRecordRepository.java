package com.tariff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.ImportRecord;

public interface ImportRecordRepository extends JpaRepository<ImportRecord, Long> {

    List<ImportRecord> findByProductId(Long productId);

    List<ImportRecord> findByUserId(Long userId);

    List<ImportRecord> findByProductIdAndUserId(Long productId, Long userId);

    List<ImportRecord> findByFromCountryCountryCode(String fromCountryCode);

    List<ImportRecord> findByToCountryCountryCode(String toCountryCode);

    List<ImportRecord> findByFromCountryCountryCodeAndToCountryCountryCode(String fromCountryCode, String toCountryCode);

    Optional<ImportRecord> findByIdAndProductId(Long id, Long productId);

    Optional<ImportRecord> findByIdAndUserId(Long id, Long userId);

    Optional<ImportRecord> findByIdAndFromCountryCountryCode(Long id, String fromCountryCode);

    Optional<ImportRecord> findByIdAndProductIdAndUserId(Long id, Long productId, Long userId);

    List<ImportRecord> findByYearBetween(int startYear, int endYear);

    List<ImportRecord> findByUserIdAndYearBetween(Long userId, int startYear, int endYear);

    // Pagination methods for calculation history
    Page<ImportRecord> findByUserId(Long userId, Pageable pageable);
}
