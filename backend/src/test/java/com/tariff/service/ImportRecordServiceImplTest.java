package com.tariff.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tariff.entity.ImportRecord;
import com.tariff.entity.Product;
import com.tariff.entity.User;
import com.tariff.entity.Country;
import com.tariff.exception.ImportRecordNotFoundException;
import com.tariff.exception.ProductNotFoundException;
import com.tariff.exception.UserNotFoundException;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.repository.ImportRecordRepository;
import com.tariff.repository.ProductRepository;
import com.tariff.repository.UserRepository;
import com.tariff.repository.CountryRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ImportRecordServiceImplTest {

    @Mock
    private ImportRecordRepository importRecordRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private ImportRecordServiceImpl importRecordService;

    private ImportRecord importRecord;
    private Product product;
    private User user;
    private Country fromCountry;
    private Country toCountry;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);

        user = new User();
        user.setId(1L);

        fromCountry = new Country();
        fromCountry.setCountryCode("US");

        toCountry = new Country();
        toCountry.setCountryCode("CN");

        importRecord = new ImportRecord();
        importRecord.setId(1L);
        importRecord.setProduct(product);
        importRecord.setUser(user);
        importRecord.setFromCountry(fromCountry);
        importRecord.setToCountry(toCountry);
        importRecord.setValue(100);
        importRecord.setYear(2025);
    }

    // --- LIST / GET ---
    @Test
    void testListImportRecord() {
        when(importRecordRepository.findAll()).thenReturn(Arrays.asList(importRecord));
        List<ImportRecord> records = importRecordService.listImportRecord();
        assertEquals(1, records.size());
        verify(importRecordRepository).findAll();
    }

    @Test
    void testGetImportRecord_Success() {
        when(importRecordRepository.findById(1L)).thenReturn(Optional.of(importRecord));
        ImportRecord record = importRecordService.getImportRecord(1L);
        assertEquals(importRecord, record);
    }

    @Test
    void testGetImportRecord_NotFound() {
        when(importRecordRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ImportRecordNotFoundException.class, () -> importRecordService.getImportRecord(2L));
    }

    // --- ADD ---
    @Test
    void testAddImportRecord() {
        when(importRecordRepository.save(importRecord)).thenReturn(importRecord);
        ImportRecord saved = importRecordService.addImportRecord(importRecord);
        assertEquals(importRecord, saved);
    }

    @Test
    void testAddImportRecordByProductAndUser_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(importRecordRepository.save(importRecord)).thenReturn(importRecord);

        ImportRecord saved = importRecordService.addImportRecordByProductAndUser(1L, 1L, importRecord);
        assertEquals(product, saved.getProduct());
        assertEquals(user, saved.getUser());
    }

    @Test
    void testAddImportRecordByProductAndUser_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () ->
            importRecordService.addImportRecordByProductAndUser(1L, 1L, importRecord));
    }

    @Test
    void testAddImportRecordByProductAndUser_UserNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () ->
            importRecordService.addImportRecordByProductAndUser(1L, 1L, importRecord));
    }

    @Test
    void testAddImportRecordByCountryPair_Success() {
        when(countryRepository.findById("US")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findById("CN")).thenReturn(Optional.of(toCountry));
        when(importRecordRepository.save(importRecord)).thenReturn(importRecord);

        ImportRecord saved = importRecordService.addImportRecordByCountryPair("US", "CN", importRecord);
        assertEquals(fromCountry, saved.getFromCountry());
        assertEquals(toCountry, saved.getToCountry());
    }

    @Test
    void testAddImportRecordByCountryPair_FromCountryNotFound() {
        when(countryRepository.findById("US")).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class, () ->
            importRecordService.addImportRecordByCountryPair("US", "CN", importRecord));
    }

    @Test
    void testAddImportRecordByCountryPair_ToCountryNotFound() {
        when(countryRepository.findById("CN")).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class, () ->
            importRecordService.addImportRecordByCountryPair("US", "CN", importRecord));
    }

    // --- GET BY FILTERS ---
    @Test
    void testGetImportRecordsByProductId_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByProductId(1L)).thenReturn(Arrays.asList(importRecord));
        List<ImportRecord> records = importRecordService.getImportRecordsByProductId(1L);
        assertEquals(1, records.size());
    }

    @Test
    void testGetImportRecordsByProductId_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> importRecordService.getImportRecordsByProductId(1L));
    }

    @Test
    void testGetImportRecordsByUserId_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByUserId(1L)).thenReturn(Arrays.asList(importRecord));
        List<ImportRecord> records = importRecordService.getImportRecordsByUserId(1L);
        assertEquals(1, records.size());
    }

    @Test
    void testGetImportRecordsByUserId_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> importRecordService.getImportRecordsByUserId(1L));
    }

    @Test
    void testGetImportRecordsByFromCountryCode_Success() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(importRecordRepository.findByFromCountryCountryCode("US")).thenReturn(Arrays.asList(importRecord));
        List<ImportRecord> records = importRecordService.getImportRecordsByFromCountryCode("US");
        assertEquals(1, records.size());
    }

    @Test
    void testGetImportRecordsByFromCountryCode_NotFound() {
        when(countryRepository.existsById("US")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () -> importRecordService.getImportRecordsByFromCountryCode("US"));
    }

    @Test
    void testGetImportRecordsByToCountryCode_Success() {
        when(countryRepository.existsById("CN")).thenReturn(true);
        when(importRecordRepository.findByToCountryCountryCode("CN")).thenReturn(Arrays.asList(importRecord));
        List<ImportRecord> records = importRecordService.getImportRecordsByToCountryCode("CN");
        assertEquals(1, records.size());
    }

    @Test
    void testGetImportRecordsByToCountryCode_NotFound() {
        when(countryRepository.existsById("CN")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () -> importRecordService.getImportRecordsByToCountryCode("CN"));
    }

    @Test
    void testGetImportRecordsByFromCountryCodeAndToCountryCode() {
        when(importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode("US", "CN"))
            .thenReturn(Arrays.asList(importRecord));
        List<ImportRecord> records = importRecordService.getImportRecordsByFromCountryCodeAndToCountryCode("US", "CN");
        assertEquals(1, records.size());
    }

    // --- UPDATE ---
    @Test
    void testUpdateImportRecord_Success() {
        when(importRecordRepository.findById(1L)).thenReturn(Optional.of(importRecord));
        when(importRecordRepository.save(importRecord)).thenReturn(importRecord);

        ImportRecord updated = importRecordService.updateImportRecord(1L, importRecord);
        assertEquals(importRecord, updated);
    }

    @Test
    void testUpdateImportRecord_NotFound() {
        when(importRecordRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ImportRecordNotFoundException.class, () -> importRecordService.updateImportRecord(2L, importRecord));
    }

    @Test
    void testUpdateImportRecordByProductAndUser_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByIdAndProductIdAndUserId(1L, 1L, 1L))
            .thenReturn(Optional.of(importRecord));
        when(importRecordRepository.save(importRecord)).thenReturn(importRecord);

        ImportRecord updated = importRecordService.updateImportRecordByProductAndUser(1L, 1L, 1L, importRecord);
        assertEquals(importRecord, updated);
    }

    @Test
    void testUpdateImportRecordByProductAndUser_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () ->
            importRecordService.updateImportRecordByProductAndUser(1L, 1L, 1L, importRecord));
    }

    @Test
    void testUpdateImportRecordByProductAndUser_UserNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () ->
            importRecordService.updateImportRecordByProductAndUser(1L, 1L, 1L, importRecord));
    }

    @Test
    void testUpdateImportRecordByProductAndUser_RecordNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByIdAndProductIdAndUserId(1L, 1L, 1L)).thenReturn(Optional.empty());
        assertThrows(ImportRecordNotFoundException.class, () ->
            importRecordService.updateImportRecordByProductAndUser(1L, 1L, 1L, importRecord));
    }

    @Test
    void testUpdateImportRecordByCountries_Success() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(countryRepository.existsById("CN")).thenReturn(true);
        when(importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode("US", "CN"))
            .thenReturn(Arrays.asList(importRecord));
        when(importRecordRepository.save(importRecord)).thenReturn(importRecord);

        ImportRecord updated = importRecordService.updateImportRecordByCountries("US", "CN", importRecord, 1L);
        assertEquals(importRecord, updated);
    }

    @Test
    void testUpdateImportRecordByCountries_FromCountryNotFound() {
        when(countryRepository.existsById("US")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () ->
            importRecordService.updateImportRecordByCountries("US", "CN", importRecord, 1L));
    }

    @Test
    void testUpdateImportRecordByCountries_ToCountryNotFound() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(countryRepository.existsById("CN")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () ->
            importRecordService.updateImportRecordByCountries("US", "CN", importRecord, 1L));
    }

    @Test
    void testUpdateImportRecordByCountries_RecordNotFound() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(countryRepository.existsById("CN")).thenReturn(true);
        when(importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode("US", "CN"))
            .thenReturn(List.of());
        assertThrows(ImportRecordNotFoundException.class, () ->
            importRecordService.updateImportRecordByCountries("US", "CN", importRecord, 1L));
    }

    // --- DELETE ---
    @Test
    void testDeleteImportRecord_Success() {
        when(importRecordRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> importRecordService.deleteImportRecord(1L));
        verify(importRecordRepository).deleteById(1L);
    }

    @Test
    void testDeleteImportRecord_NotFound() {
        when(importRecordRepository.existsById(1L)).thenReturn(false);
        assertThrows(ImportRecordNotFoundException.class, () ->
            importRecordService.deleteImportRecord(1L));
    }

    @Test
    void testDeleteImportRecordByProductAndUser_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByIdAndProductIdAndUserId(1L, 1L, 1L))
            .thenReturn(Optional.of(importRecord));

        assertDoesNotThrow(() -> importRecordService.deleteImportRecordByProductAndUser(1L, 1L, 1L));
        verify(importRecordRepository).delete(importRecord);
    }

    @Test
    void testDeleteImportRecordByProductAndUser_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () ->
            importRecordService.deleteImportRecordByProductAndUser(1L, 1L, 1L));
    }

    @Test
    void testDeleteImportRecordByProductAndUser_UserNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () ->
            importRecordService.deleteImportRecordByProductAndUser(1L, 1L, 1L));
    }

    @Test
    void testDeleteImportRecordByProductAndUser_RecordNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByIdAndProductIdAndUserId(1L, 1L, 1L)).thenReturn(Optional.empty());
        assertThrows(ImportRecordNotFoundException.class, () ->
            importRecordService.deleteImportRecordByProductAndUser(1L, 1L, 1L));
    }

    @Test
    void testDeleteImportRecordByCountries_Success() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(countryRepository.existsById("CN")).thenReturn(true);
        when(importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode("US", "CN"))
            .thenReturn(Arrays.asList(importRecord));

        assertDoesNotThrow(() -> importRecordService.deleteImportRecordByCountries("US", "CN", 1L));
        verify(importRecordRepository).delete(importRecord);
    }

    @Test
    void testDeleteImportRecordByCountries_FromCountryNotFound() {
        when(countryRepository.existsById("US")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () ->
            importRecordService.deleteImportRecordByCountries("US", "CN", 1L));
    }

    @Test
    void testDeleteImportRecordByCountries_ToCountryNotFound() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(countryRepository.existsById("CN")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () ->
            importRecordService.deleteImportRecordByCountries("US", "CN", 1L));
    }

    @Test
    void testDeleteImportRecordByCountries_RecordNotFound() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(countryRepository.existsById("CN")).thenReturn(true);
        when(importRecordRepository.findByFromCountryCountryCodeAndToCountryCountryCode("US", "CN"))
            .thenReturn(List.of());
        assertThrows(ImportRecordNotFoundException.class, () ->
            importRecordService.deleteImportRecordByCountries("US", "CN", 1L));
    }
}