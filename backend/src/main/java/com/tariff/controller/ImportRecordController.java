package com.tariff.controller;

import com.tariff.entity.ImportRecord;
import com.tariff.entity.User;
import com.tariff.entity.Product;
import com.tariff.dto.request.SaveCalculationRequest;
import com.tariff.service.ImportRecordService;
import com.tariff.service.ProductService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/import-records")
public class ImportRecordController {

    private final ImportRecordService importRecordService;
    private final ProductService productService;

    public ImportRecordController(ImportRecordService importRecordService, ProductService productService) {
        this.importRecordService = importRecordService;
        this.productService = productService;
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

    @PostMapping("/save-calculation")
    @Operation(summary = "Save a tariff calculation to history")
    public ResponseEntity<?> saveCalculation(
            @RequestBody SaveCalculationRequest request,
            Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        Long userId = importRecordService.getUserIdFromAuthentication(authentication);
        Long productId = request.getProductId();

        // Get the product if provided
        Product product = null;
        if (productId != null && productId > 0) {
            product = productService.getProduct(productId);
        }

        ImportRecord record = new ImportRecord(
                request.getValue(),
                request.getYear(),
                request.getTariffRate(),
                request.getCalculatedTariff(),
                request.getAdditionalFeeRate(),
                request.getTotalAdditionalFees(),
                request.getTotalCost(),
                product,
                request.getCalculationType()
        );

        ImportRecord saved = importRecordService.addImportRecordByProductAndUser(
                productId,
                userId,
                record
        );

        // Set countries by code
        saved = importRecordService.addImportRecordByCountryPair(
                request.getFromCountryId(),
                request.getToCountryId(),
                saved
        );

        return ResponseEntity.ok(saved);
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
        return importRecordService.updateImportRecordByProductAndUser(productId, userId, id, importRecord);
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
        importRecordService.deleteImportRecordByProductAndUser(productId, userId, id);
        return ResponseEntity.ok().build();
    }

    // Calculation history endpoints
    @GetMapping("/history")
    @Operation(summary = "Get calculation history for current user with pagination")
    public Page<ImportRecord> getCalculationHistory(
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Get the user ID from the authentication principal or service
        Long userId = importRecordService.getUserIdFromAuthentication(authentication);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return importRecordService.getUserCalculationHistory(userId, pageable);
    }

    @GetMapping("/admin/history")
    @Operation(summary = "Get all calculation history for admin with pagination")
    public Page<ImportRecord> getAllCalculationHistoryAdmin(
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Verify user is admin
        if (!authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Only admins can access this endpoint");
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        return importRecordService.getAllCalculationHistory(pageable);
    }

    @DeleteMapping("/history/{id}")
    @Operation(summary = "Delete a specific calculation from history")
    public ResponseEntity<?> deleteCalculationHistory(
            @PathVariable Long id,
            Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        Long userId = importRecordService.getUserIdFromAuthentication(authentication);
        importRecordService.deleteCalculationHistory(id, userId);
        return ResponseEntity.ok().build();
    }
}
