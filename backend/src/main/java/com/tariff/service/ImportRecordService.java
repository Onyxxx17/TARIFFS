package com.tariff.service;

import java.util.List;

import com.tariff.entity.ImportRecord;

public interface ImportRecordService {

    List<ImportRecord> listImportRecord();

    ImportRecord getImportRecord(Long id);

    List<ImportRecord> getImportRecordsByProductId(Long productId);

    List<ImportRecord> getImportRecordsByUserId(Long userId);

    ImportRecord addImportRecord(ImportRecord importRecord);

    ImportRecord addImportRecordByProductAndUser(Long productId, Long userId, ImportRecord importRecord);

    ImportRecord updateImportRecord(Long id, ImportRecord importRecord);

    ImportRecord updateImportRecord(Long productId, Long userId, Long id, ImportRecord importRecord);

    void deleteImportRecord(Long id);

    void deleteImportRecord(Long productId, Long userId, Long id);
}
