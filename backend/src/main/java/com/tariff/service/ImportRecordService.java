package com.tariff.service;

import java.util.List;

import com.tariff.entity.ImportRecord;

public interface ImportRecordService {
    List<ImportRecord> listImportRecord();
    ImportRecord getImportRecord(Long id);
    ImportRecord addImportRecord(ImportRecord importRecord);
    ImportRecord updateImportRecord(Long id, ImportRecord importRecord);
    void deleteImportRecord(Long id);
}
