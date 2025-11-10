package com.tariff.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tariff.entity.Country;
import com.tariff.entity.Product;
import com.tariff.entity.TariffRule;
import com.tariff.exception.CountryNotFoundException;
import com.tariff.exception.ProductNotFoundException;
import com.tariff.exception.TariffRuleNotFoundException;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.ProductRepository;
import com.tariff.repository.TariffRuleRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TariffRuleServiceImplTest {

    @Mock
    private TariffRuleRepository tariffRuleRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private TariffRuleServiceImpl tariffRuleService;

    private Country fromCountry;
    private Country toCountry;
    private Product product;
    private TariffRule tariffRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fromCountry = new Country();
        fromCountry.setCountryCode("US");

        toCountry = new Country();
        toCountry.setCountryCode("CN");

        product = new Product();
        product.setId(1L);

        tariffRule = new TariffRule();
        tariffRule.setId(100L);
        tariffRule.setFromCountry(fromCountry);
        tariffRule.setToCountry(toCountry);
        tariffRule.setProduct(product);
        tariffRule.setRate(new BigDecimal("5"));
    
        when(countryRepository.findById("US")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findById("CN")).thenReturn(Optional.of(toCountry));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(tariffRuleRepository.findById(100L)).thenReturn(Optional.of(tariffRule));
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));

        when(countryRepository.existsById("US")).thenReturn(true);
        when(countryRepository.existsById("CN")).thenReturn(true);
        when(productRepository.existsById(1L)).thenReturn(true);
        when(tariffRuleRepository.existsById(100L)).thenReturn(true);
        
    }

 @Test
    void testListTariffRule() {
        when(tariffRuleRepository.findAll()).thenReturn(Arrays.asList(tariffRule));
        List<TariffRule> result = tariffRuleService.listTariffRule();
        assertEquals(1, result.size());
        verify(tariffRuleRepository).findAll();
    }

    // ---------------- GET ----------------
    @Test
    void testGetTariffRuleFound() {
        when(tariffRuleRepository.findById(100L)).thenReturn(Optional.of(tariffRule));
        TariffRule result = tariffRuleService.getTariffRule(100L);
        assertEquals(tariffRule, result);
    }

    @Test
    void testGetTariffRuleNotFound() {
        when(tariffRuleRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(TariffRuleNotFoundException.class, () -> tariffRuleService.getTariffRule(100L));
    }

    @Test
    void testGetTariffRulesByFromCountryCodeFound() {
        when(tariffRuleRepository.findByFromCountryCountryCode("US"))
                .thenReturn(List.of(tariffRule));

        List<TariffRule> result = tariffRuleService.getTariffRulesByFromCountryCode("US");
        assertEquals(tariffRule, result.get(0));
    }

    @Test
    void testGetTariffRulesByFromCountryCodeNotFound() {
        when(countryRepository.existsById("XX")).thenReturn(false);

        assertThrows(CountryNotFoundException.class, () ->
                tariffRuleService.getTariffRulesByFromCountryCode("XX"));
    }

    @Test
    void testGetTariffRulesByToCountryCodeFound() {
        when(tariffRuleRepository.findByToCountryCountryCode("CN"))
                .thenReturn(List.of(tariffRule));

        List<TariffRule> result = tariffRuleService.getTariffRulesByToCountryCode("CN");
        assertEquals(tariffRule, result.get(0));
    }

    @Test
    void testGetTariffRulesByToCountryCodeNotFound() {
        when(countryRepository.existsById("XX")).thenReturn(false);

        assertThrows(CountryNotFoundException.class, () ->
                tariffRuleService.getTariffRulesByToCountryCode("XX"));
    }

    // ---------------- ADD ----------------
    @Test
    void testAddTariffRule() {
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));
        TariffRule result = tariffRuleService.addTariffRule(tariffRule);
        assertEquals(tariffRule, result);
        verify(tariffRuleRepository).save(tariffRule);
    }

    @Test
    void testAddTariffRuleByCountryAndProduct_Success() {
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));

        TariffRule result = tariffRuleService.addTariffRuleByCountryAndProduct("US", 1L, tariffRule);
        assertEquals(fromCountry, result.getFromCountry());
        assertEquals(fromCountry, result.getToCountry());
        assertEquals(product, result.getProduct());
        verify(tariffRuleRepository).save(result);
    }

    @Test
    void testAddTariffRuleByCountryAndProduct_CountryNotFound() {
        when(countryRepository.findById("US")).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class,
            () -> tariffRuleService.addTariffRuleByCountryAndProduct("US", 1L, tariffRule));
    }

    @Test
    void testAddTariffRuleByCountryAndProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
            () -> tariffRuleService.addTariffRuleByCountryAndProduct("US", 1L, tariffRule));
    }

    @Test
    void testAddTariffRuleByCountriesAndProduct_Success() {
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));

        TariffRule result = tariffRuleService.addTariffRuleByCountriesAndProduct("US", "CN", 1L, tariffRule);
        assertEquals(fromCountry, result.getFromCountry());
        assertEquals(toCountry, result.getToCountry());
        assertEquals(product, result.getProduct());
        verify(tariffRuleRepository).save(result);
    }

    @Test
    void testAddTariffRuleByCountriesAndProduct_FromCountryNotFound() {
        when(countryRepository.findById("US")).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class,
            () -> tariffRuleService.addTariffRuleByCountriesAndProduct("US", "CN", 1L, tariffRule));
    }

    @Test
    void testAddTariffRuleByCountriesAndProduct_ToCountryNotFound() {
        when(countryRepository.findById("CN")).thenReturn(Optional.empty());
        assertThrows(CountryNotFoundException.class,
            () -> tariffRuleService.addTariffRuleByCountriesAndProduct("US", "CN", 1L, tariffRule));
    }

    @Test
    void testAddTariffRuleByCountriesAndProduct_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class,
            () -> tariffRuleService.addTariffRuleByCountriesAndProduct("US", "CN", 1L, tariffRule));
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateTariffRuleById_Success() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        update.setAdditionalFee(new BigDecimal("10"));
        update.setProduct(product);
        update.setFromCountry(fromCountry);
        update.setToCountry(toCountry);
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));
        TariffRule result = tariffRuleService.updateTariffRule(100L, update);
        assertEquals(new BigDecimal("10"), result.getRate());
        assertEquals(new BigDecimal("10"), result.getAdditionalFee());
        assertEquals(product, result.getProduct());
        assertEquals(fromCountry, result.getFromCountry());
        assertEquals(toCountry, result.getToCountry());
    }

    @Test
    void testUpdateTariffRuleById_SuccessWithNullValues() {
        TariffRule update = new TariffRule();
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));
        TariffRule result = tariffRuleService.updateTariffRule(100L, update);
        assertEquals(new BigDecimal("5"), result.getRate());
        assertEquals(null, result.getAdditionalFee());
        assertEquals(product, result.getProduct());
        assertEquals(fromCountry, result.getFromCountry());
        assertEquals(toCountry, result.getToCountry());
    }

    @Test
    void testUpdateTariffRuleById_NotFound() {
        TariffRule update = new TariffRule();
        when(tariffRuleRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(TariffRuleNotFoundException.class, () -> tariffRuleService.updateTariffRule(100L, update));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_Success_withListLookup() {
        TariffRule existingRule = new TariffRule();
        existingRule.setId(100L);
        existingRule.setFromCountry(fromCountry);
        existingRule.setToCountry(toCountry);
        existingRule.setProduct(product);
        existingRule.setRate(new BigDecimal("10"));
        existingRule.setAdditionalFee(new BigDecimal("10"));

        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        update.setAdditionalFee(new BigDecimal("10"));

        TariffRule result = tariffRuleService.updateTariffRule("US", "CN", 1L, 100L, update);
        assertEquals(new BigDecimal("10"), result.getRate());
        assertEquals(new BigDecimal("10"), result.getAdditionalFee());
        verify(tariffRuleRepository).save(existingRule);
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongTariffId() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(TariffRuleNotFoundException.class, () ->
                tariffRuleService.updateTariffRule("US", "CN", 1L, 200L, update));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_NullRate() {
        TariffRule update = new TariffRule();
        update.setRate(null);
        TariffRule result = tariffRuleService.updateTariffRule("US", "CN", 1L, 100L, update);
        assertEquals(new BigDecimal("5"), result.getRate());
    }
    
    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongFromCountryCode() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(CountryNotFoundException.class, () ->
                tariffRuleService.updateTariffRule("XX", "CN", 1L, 100L, update));
    }
    
    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongToCountryCode() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(CountryNotFoundException.class, () ->
                tariffRuleService.updateTariffRule("US", "XX", 1L, 100L, update));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongProduct() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(ProductNotFoundException.class, () ->
                tariffRuleService.updateTariffRule("US", "CN", 2L, 100L, update));
    }

    // ---------------- DELETE ----------------
    @Test
    void testDeleteTariffRuleById_Success() {
        tariffRuleService.deleteTariffRule(100L);
        verify(tariffRuleRepository).deleteById(100L);
    }

    @Test
    void testDeleteTariffRuleById_NotFound() {
        when(tariffRuleRepository.existsById(100L)).thenReturn(false);
        assertThrows(TariffRuleNotFoundException.class, () -> tariffRuleService.deleteTariffRule(100L));
    }

    @Test
    void testDeleteTariffRuleByCountry_NotFound() {
        when(countryRepository.findById("XX")).thenReturn(null);
        assertThrows(CountryNotFoundException.class, () -> tariffRuleService.deleteTariffRule("XX", 1L, 100L));
    }

    @Test
    void testDeleteTariffRuleByProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(null);
        assertThrows(ProductNotFoundException.class, () -> tariffRuleService.deleteTariffRule("US", 2L, 100L));
    }

    @Test
    void testDeleteTariffRule_ByCodesAndIds_WrongFromCountryCode() {
        assertThrows(CountryNotFoundException.class, () ->
                tariffRuleService.deleteTariffRule("XX", "CN", 1L, 100L));
    }
    
    @Test
    void testDeleteTariffRule_ByCodesAndIds_WrongToCountryCode() {
        assertThrows(CountryNotFoundException.class, () ->
                tariffRuleService.deleteTariffRule("US", "XX", 1L, 100L));
    }

    @Test
    void testDeleteTariffRuleByProduct_NotFound2() {
        when(productRepository.findById(1L)).thenReturn(null);
        assertThrows(ProductNotFoundException.class, () -> tariffRuleService.deleteTariffRule("US", "CN", 2L, 100L));
    }

    @Test
    void testDeleteTariffRule_ByCountryAndID() {
        TariffRule existingRule = new TariffRule();
        existingRule.setId(100L);
        existingRule.setFromCountry(fromCountry);
        existingRule.setToCountry(toCountry);
        existingRule.setProduct(product);
        existingRule.setRate(new BigDecimal("5"));

        when(tariffRuleRepository.findByFromCountryCountryCodeAndToCountryCountryCodeAndProductId("US", "CN", 1L))
                .thenReturn(List.of(existingRule));

        tariffRuleService.deleteTariffRule("US", 1L, 100L);
        verify(tariffRuleRepository).delete(existingRule);
    }

    @Test
    void testDeleteTariffRule_ByCodesAndIds_Success_withListLookup() {
        TariffRule existingRule = new TariffRule();
        existingRule.setId(100L);
        existingRule.setFromCountry(fromCountry);
        existingRule.setToCountry(toCountry);
        existingRule.setProduct(product);
        existingRule.setRate(new BigDecimal("5"));

        when(tariffRuleRepository.findByFromCountryCountryCodeAndToCountryCountryCodeAndProductId("US", "CN", 1L))
                .thenReturn(List.of(existingRule));

        tariffRuleService.deleteTariffRule("US", "CN", 1L, 100L);
        verify(tariffRuleRepository).delete(existingRule);
    }

    // ---------------- GET BY COUNTRY ----------------
    @Test
    void testGetTariffRulesByCountryCode_Success() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(tariffRuleRepository.findByFromCountryCountryCodeOrToCountryCountryCode("US", "US"))
                .thenReturn(Arrays.asList(tariffRule));

        List<TariffRule> result = tariffRuleService.getTariffRulesByCountryCode("US");
        assertEquals(1, result.size());
    }

    @Test
    void testGetTariffRulesByCountryCode_CountryNotFound() {
        when(countryRepository.existsById("XX")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () -> tariffRuleService.getTariffRulesByCountryCode("XX"));
    }

    @Test
    void testGetTariffRulesByFromCountryCode() {
        when(countryRepository.existsById("US")).thenReturn(true);
        when(tariffRuleRepository.findByFromCountryCountryCode("US")).thenReturn(Arrays.asList(tariffRule));

        List<TariffRule> result = tariffRuleService.getTariffRulesByFromCountryCode("US");
        assertEquals(1, result.size());
    }

    @Test
    void testGetTariffRulesByToCountryCode() {
        when(countryRepository.existsById("CN")).thenReturn(true);
        when(tariffRuleRepository.findByToCountryCountryCode("CN")).thenReturn(Arrays.asList(tariffRule));

        List<TariffRule> result = tariffRuleService.getTariffRulesByToCountryCode("CN");
        assertEquals(1, result.size());
    }

    @Test
    void testGetTariffRulesByProductId() {
        when(productRepository.existsById(1L)).thenReturn(true);
        when(tariffRuleRepository.findByProductId(1L)).thenReturn(Arrays.asList(tariffRule));

        List<TariffRule> result = tariffRuleService.getTariffRulesByProductId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetTariffRulesByProductId_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> tariffRuleService.getTariffRulesByProductId(1L));
    }

}