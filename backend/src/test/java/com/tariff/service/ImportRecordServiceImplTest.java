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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;

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
        assertThrows(ProductNotFoundException.class, ()
                -> importRecordService.addImportRecordByProductAndUser(1L, 1L, importRecord));
    }

    @Test
    void testAddImportRecordByProductAndUser_UserNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, ()
                -> importRecordService.addImportRecordByProductAndUser(1L, 1L, importRecord));
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
        assertThrows(CountryNotFoundException.class, ()
                -> importRecordService.addImportRecordByCountryPair("US", "CN", importRecord));
    }

    @Test
    void testAddImportRecordByCountryPair_ToCountryNotFound() {
        when(countryRepository.findById("CN")).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class, ()
                -> importRecordService.addImportRecordByCountryPair("US", "CN", importRecord));
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
        assertThrows(ProductNotFoundException.class, ()
                -> importRecordService.updateImportRecordByProductAndUser(1L, 1L, 1L, importRecord));
    }

    @Test
    void testUpdateImportRecordByProductAndUser_UserNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, ()
                -> importRecordService.updateImportRecordByProductAndUser(1L, 1L, 1L, importRecord));
    }

    @Test
    void testUpdateImportRecordByProductAndUser_RecordNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByIdAndProductIdAndUserId(1L, 1L, 1L)).thenReturn(Optional.empty());
        assertThrows(ImportRecordNotFoundException.class, ()
                -> importRecordService.updateImportRecordByProductAndUser(1L, 1L, 1L, importRecord));
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
        assertThrows(ImportRecordNotFoundException.class, ()
                -> importRecordService.deleteImportRecord(1L));
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
        assertThrows(ProductNotFoundException.class, ()
                -> importRecordService.deleteImportRecordByProductAndUser(1L, 1L, 1L));
    }

    @Test
    void testDeleteImportRecordByProductAndUser_UserNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(UserNotFoundException.class, ()
                -> importRecordService.deleteImportRecordByProductAndUser(1L, 1L, 1L));
    }

    @Test
    void testDeleteImportRecordByProductAndUser_RecordNotFound() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByIdAndProductIdAndUserId(1L, 1L, 1L)).thenReturn(Optional.empty());
        assertThrows(ImportRecordNotFoundException.class, ()
                -> importRecordService.deleteImportRecordByProductAndUser(1L, 1L, 1L));
    }

    // --- NEW TESTS FOR HIGHLIGHTED CODE ---

    @Test
    void testGetUserIdFromAuthentication_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Long userId = importRecordService.getUserIdFromAuthentication(authentication);

        assertEquals(1L, userId);
    }

    @Test
    void testGetUserIdFromAuthentication_UserNotFound() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("notfound@example.com");
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> importRecordService.getUserIdFromAuthentication(authentication));
    }

    @Test
    void testGetUserCalculationHistory_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ImportRecord> page = new PageImpl<>(Arrays.asList(importRecord));
        when(userRepository.existsById(1L)).thenReturn(true);
        when(importRecordRepository.findByUserId(1L, pageable)).thenReturn(page);

        Page<ImportRecord> result = importRecordService.getUserCalculationHistory(1L, pageable);

        assertEquals(1, result.getTotalElements());
        verify(importRecordRepository).findByUserId(1L, pageable);
    }

    @Test
    void testGetUserCalculationHistory_UserNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> importRecordService.getUserCalculationHistory(2L, pageable));
    }

    @Test
    void testGetAllCalculationHistory() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ImportRecord> page = new PageImpl<>(Arrays.asList(importRecord));
        when(importRecordRepository.findAll(pageable)).thenReturn(page);

        Page<ImportRecord> result = importRecordService.getAllCalculationHistory(pageable);

        assertEquals(1, result.getTotalElements());
        verify(importRecordRepository).findAll(pageable);
    }

    @Test
    void testDeleteCalculationHistory_Success() {
        when(importRecordRepository.findById(1L)).thenReturn(Optional.of(importRecord));

        assertDoesNotThrow(() -> importRecordService.deleteCalculationHistory(1L, 1L));

        verify(importRecordRepository).deleteById(1L);
    }

    @Test
    void testDeleteCalculationHistory_RecordNotFound() {
        when(importRecordRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ImportRecordNotFoundException.class, () -> importRecordService.deleteCalculationHistory(2L, 1L));
    }

    @Test
    void testDeleteCalculationHistory_UserMismatch() {
        when(importRecordRepository.findById(1L)).thenReturn(Optional.of(importRecord));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> importRecordService.deleteCalculationHistory(1L, 2L));
        assertEquals("You can only delete your own calculation history", exception.getMessage());
    }

    @Test
    void testGetCountryCodeByCountryName_Success() {
        when(countryRepository.findCountryByName("United States")).thenReturn(Optional.of(fromCountry));

        String countryCode = importRecordService.getCountryCodeByCountryName("United States");

        assertEquals("US", countryCode);
    }

    @Test
    void testGetCountryCodeByCountryName_NotFound() {
        when(countryRepository.findCountryByName("Unknown Country")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> importRecordService.getCountryCodeByCountryName("Unknown Country"));
        assertEquals("Country not found: Unknown Country", exception.getMessage());
    }
}
