package com.tariff.service;

import java.util.List;

import com.tariff.entity.ImportRecord;

public interface ImportRecordService {

    List<ImportRecord> listImportRecord();

    ImportRecord getImportRecord(Long id);

    List<ImportRecord> getImportRecordsByProductId(Long productId);

    List<ImportRecord> getImportRecordsByUserId(Long userId);

    List<ImportRecord> getImportRecordsByFromCountryId(Long fromCountryId);

    List<ImportRecord> getImportRecordsByToCountryId(Long toCountryId);

    List<ImportRecord> getImportRecordsByFromCountryIdAndToCountryId(Long fromCountryId, Long toCountryId);

    ImportRecord addImportRecord(ImportRecord importRecord);

    ImportRecord addImportRecordByProductAndUser(Long productId, Long userId, ImportRecord importRecord);

    ImportRecord addImportRecordByCountryPair(Long fromCountryId, Long toCountryId, ImportRecord importRecord);

    ImportRecord updateImportRecord(Long id, ImportRecord importRecord);

    ImportRecord updateImportRecordByProductAndUser(Long productId, Long userId, Long id, ImportRecord importRecord);

    ImportRecord updateImportRecordByCountries(Long fromCountryId, Long toCountryId, ImportRecord importRecord, Long id);

    void deleteImportRecord(Long id);

    void deleteImportRecordByProductAndUser(Long productId, Long userId, Long id);

    void deleteImportRecordByCountries(Long fromCountryId, Long toCountryId, Long id);
}
