package com.tariff.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tariff.entity.ImportRecord;

public interface ImportRecordRepository extends JpaRepository<ImportRecord, Long> {
    List<ImportRecord> findByProductId(Long productId);
    List<ImportRecord> findByUserId(Long userId);
    List<ImportRecord> findByProductIdAndUserId(Long productId, Long userId);
    Optional<ImportRecord> findByIdAndProductId(Long id, Long productId);
    Optional<ImportRecord> findByIdAndUserId(Long id, Long userId);
    Optional<ImportRecord> findByIdAndProductIdAndUserId(Long id, Long productId, Long userId);
    List<ImportRecord> findByDateBetween(Date startDate, Date endDate);
    List<ImportRecord> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate);
}
