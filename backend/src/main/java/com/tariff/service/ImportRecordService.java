package com.tariff.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.tariff.entity.ImportRecord;

public interface ImportRecordService {

    List<ImportRecord> listImportRecord();

    ImportRecord getImportRecord(Long id);

    List<ImportRecord> getImportRecordsByProductId(Long productId);

    List<ImportRecord> getImportRecordsByUserId(Long userId);

    List<ImportRecord> getImportRecordsByFromCountryCode(String fromCountryCode);

    List<ImportRecord> getImportRecordsByToCountryCode(String toCountryCode);

    List<ImportRecord> getImportRecordsByFromCountryCodeAndToCountryCode(String fromCountryCode, String toCountryCode);

    ImportRecord addImportRecord(ImportRecord importRecord);

    ImportRecord addImportRecordByProductAndUser(Long productId, Long userId, ImportRecord importRecord);

    ImportRecord addImportRecordByCountryPair(String fromCountryCode, String toCountryCode, ImportRecord importRecord);

    ImportRecord updateImportRecord(Long id, ImportRecord importRecord);

    ImportRecord updateImportRecordByProductAndUser(Long productId, Long userId, Long id, ImportRecord importRecord);

    ImportRecord updateImportRecordByCountries(String fromCountryCode, String toCountryCode, ImportRecord importRecord, Long id);

    void deleteImportRecord(Long id);

    void deleteImportRecordByProductAndUser(Long productId, Long userId, Long id);

    void deleteImportRecordByCountries(String fromCountryCode, String toCountryCode, Long id);

    // Calculation history methods
    Long getUserIdFromAuthentication(Authentication authentication);

    Page<ImportRecord> getUserCalculationHistory(Long userId, Pageable pageable);

    Page<ImportRecord> getAllCalculationHistory(Pageable pageable);

    void deleteCalculationHistory(Long id, Long userId);
}
