package com.tariff.controller;

import com.tariff.entity.ImportRecord;
import com.tariff.service.ImportRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import-records")
public class ImportRecordController {
    
    private ImportRecordService importRecordService;
    
    public ImportRecordController(ImportRecordService importRecordService) {
        this.importRecordService = importRecordService;
    }
    
    @GetMapping
    public List<ImportRecord> getAllImportRecords() {
        return importRecordService.listImportRecord();
    }
    
    @GetMapping("/{id}")
    public ImportRecord getImportRecordById(@PathVariable Long id) {
        return importRecordService.getImportRecord(id);
    }
    
    @GetMapping("/product/{productId}")
    public List<ImportRecord> getImportRecordsByProduct(@PathVariable Long productId) {
        return importRecordService.getImportRecordsByProductId(productId);
    }
    
    @GetMapping("/user/{userId}")
    public List<ImportRecord> getImportRecordsByUser(@PathVariable Long userId) {
        return importRecordService.getImportRecordsByUserId(userId);
    }
    
    @PostMapping
    public ImportRecord createImportRecord(@RequestBody ImportRecord importRecord) {
        return importRecordService.addImportRecord(importRecord);
    }
    
    @PostMapping("/product/{productId}/user/{userId}")
    public ImportRecord createImportRecordWithProductAndUser(
            @PathVariable Long productId,
            @PathVariable Long userId,
            @RequestBody ImportRecord importRecord) {
        return importRecordService.addImportRecordByProductAndUser(productId, userId, importRecord);
    }
    
    @PutMapping("/{id}")
    public ImportRecord updateImportRecord(@PathVariable Long id, @RequestBody ImportRecord importRecord) {
        return importRecordService.updateImportRecord(id, importRecord);
    }
    
    @PutMapping("/product/{productId}/user/{userId}/{id}")
    public ImportRecord updateImportRecordWithProductAndUser(
            @PathVariable Long productId,
            @PathVariable Long userId,
            @PathVariable Long id,
            @RequestBody ImportRecord importRecord) {
        return importRecordService.updateImportRecord(productId, userId, id, importRecord);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImportRecord(@PathVariable Long id) {
        importRecordService.deleteImportRecord(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/product/{productId}/user/{userId}/{id}")
    public ResponseEntity<?> deleteImportRecordWithProductAndUser(
            @PathVariable Long productId,
            @PathVariable Long userId,
            @PathVariable Long id) {
        importRecordService.deleteImportRecord(productId, userId, id);
        return ResponseEntity.ok().build();
    }
}