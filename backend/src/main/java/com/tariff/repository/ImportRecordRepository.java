package com.tariff.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tariff.entity.ImportRecord;

@Repository
public interface ImportRecordRepository extends JpaRepository<ImportRecord, Long> {
    
}
