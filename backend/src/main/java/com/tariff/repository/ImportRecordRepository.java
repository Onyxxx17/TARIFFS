package com.tariff.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.Country;
import com.tariff.entity.ImportRecord;

public interface ImportRecordRepository extends JpaRepository<ImportRecord, Long> {
    List<ImportRecord> findByProductId(Long productId);
    List<ImportRecord> findByUserId(Long userId);
    List<ImportRecord> findByProductIdAndUserId(Long productId, Long userId);
    List<ImportRecord> findByFromCountryId(Long fromCountryId);
    List<ImportRecord> findByToCountryId(Long toCountryId);
    // List<ImportRecord> findByFromAndToCountryId(Long fromCountryId, Long toCountryId);
    Optional<ImportRecord> findByIdAndProductId(Long id, Long productId);
    Optional<ImportRecord> findByIdAndUserId(Long id, Long userId);
    Optional<ImportRecord> findByIdAndFromCountryId(Long id, Long fromCountryId);
    Optional<ImportRecord> findByIdAndProductIdAndUserId(Long id, Long productId, Long userId);
    // Optional<ImportRecord> findOptionalByFromAndToCountryId(Long fromCountryId, Long toCountryId);
    List<ImportRecord> findByYearBetween(int startYear, int endYear);
    List<ImportRecord> findByUserIdAndYearBetween(Long userId, int startYear, int endYear);
}
