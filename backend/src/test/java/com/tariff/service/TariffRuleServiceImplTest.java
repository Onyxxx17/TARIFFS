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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        fromCountry.setCountryCode("C840");

        toCountry = new Country();
        toCountry.setCountryCode("C156");

        product = new Product();
        product.setId(1L);

        tariffRule = new TariffRule();
        tariffRule.setId(100L);
        tariffRule.setFromCountry(fromCountry);
        tariffRule.setToCountry(toCountry);
        tariffRule.setProduct(product);
        tariffRule.setRate(new BigDecimal("5"));

        when(countryRepository.findById("C840")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findById("C156")).thenReturn(Optional.of(toCountry));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(tariffRuleRepository.findById(100L)).thenReturn(Optional.of(tariffRule));
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));

        when(countryRepository.existsById("C840")).thenReturn(true);
        when(countryRepository.existsById("C156")).thenReturn(true);
        when(productRepository.existsById(1L)).thenReturn(true);
        when(tariffRuleRepository.existsById(100L)).thenReturn(true);

    }

    @Test
    void testListTariffRule() {
        Page<TariffRule> page = new PageImpl<>(Arrays.asList(tariffRule));
        when(tariffRuleRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<TariffRule> result = tariffRuleService.listTariffRule(Pageable.unpaged());
        assertEquals(1, result.getContent().size());
        verify(tariffRuleRepository).findAll(any(Pageable.class));
    }    // ---------------- GET ----------------

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

    // @Test
    // void testGetTariffRulesByFromCountryCodeFound() {
    //     Page<TariffRule> page = new PageImpl<>(List.of(tariffRule));
    //     when(tariffRuleRepository.findByFromCountryCountryCode(eq("US"), any(Pageable.class)))
    //             .thenReturn(page);
    //     Page<TariffRule> result = tariffRuleService.getTariffRulesByFromCountryCode("C840", Pageable.unpaged());
    //     assertEquals(tariffRule, result.getContent().get(0));
    // }
    @Test
    void testGetTariffRulesByToCountryCodeFound() {
        Page<TariffRule> page = new PageImpl<>(List.of(tariffRule));
        when(tariffRuleRepository.findByToCountryCountryCode(eq("C840"), any(Pageable.class)))
                .thenReturn(page);

        Page<TariffRule> result = tariffRuleService.getTariffRulesByToCountryCode("C840", Pageable.unpaged());
        assertEquals(tariffRule, result.getContent().get(0));
    }

    @Test
    void testGetTariffRulesByToCountryCodeNotFound() {
        when(countryRepository.existsById("XX")).thenReturn(false);

        assertThrows(CountryNotFoundException.class, ()
                -> tariffRuleService.getTariffRulesByToCountryCode("XX", Pageable.unpaged()));
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
    void testAddTariffRuleByCountriesAndProduct_Success() {
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));

        TariffRule result = tariffRuleService.addTariffRuleByCountriesAndProduct("C840", "C156", 1L, tariffRule);
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
                () -> tariffRuleService.addTariffRuleByCountriesAndProduct("C840", "C156", 1L, tariffRule));
    }

    // ---------------- UPDATE ----------------
    @Test
    void testUpdateTariffRuleById_Success() {
        TariffRule update = new TariffRule(new BigDecimal("10"), Arrays.asList(new BigDecimal("10")), 2024);
        update.setProduct(product);
        update.setFromCountry(fromCountry);
        update.setToCountry(toCountry);
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));
        TariffRule result = tariffRuleService.updateTariffRule(100L, update);
        assertEquals(new BigDecimal("10"), result.getRate());
        assertEquals(Arrays.asList(new BigDecimal("10")), result.getAdditionalFees());
        assertEquals(product, result.getProduct());
        assertEquals(fromCountry, result.getFromCountry());
        assertEquals(toCountry, result.getToCountry());
    }

    @Test
    void testUpdateTariffRuleById_SuccessWithNullValues() {
        TariffRule update = new TariffRule(new BigDecimal("5"), 2024);
        when(tariffRuleRepository.save(any(TariffRule.class))).thenAnswer(inv -> inv.getArgument(0));
        TariffRule result = tariffRuleService.updateTariffRule(100L, update);
        assertEquals(new BigDecimal("5"), result.getRate());
        assertEquals(List.of(), result.getAdditionalFees());
        assertEquals(product, result.getProduct());
        assertEquals(fromCountry, result.getFromCountry());
        assertEquals(toCountry, result.getToCountry());
    }

    @Test
    void testUpdateTariffRuleById_NotFound() {
        TariffRule update = new TariffRule(new BigDecimal("5"), 2024);
        when(tariffRuleRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(TariffRuleNotFoundException.class, () -> tariffRuleService.updateTariffRule(100L, update));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_Success_withListLookup() {
        TariffRule existingRule = new TariffRule(new BigDecimal("5"), Arrays.asList(new BigDecimal("5")), 2024);
        existingRule.setId(100L);
        existingRule.setFromCountry(fromCountry);
        existingRule.setToCountry(toCountry);
        existingRule.setProduct(product);

        TariffRule update = new TariffRule(new BigDecimal("10"), Arrays.asList(new BigDecimal("10")), 2024);

        when(tariffRuleRepository.findByFromCountryCountryCodeAndToCountryCountryCodeAndProductId(
                eq("C840"), eq("C156"), eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(existingRule)));
        when(tariffRuleRepository.findById(100L)).thenReturn(Optional.of(existingRule));
        when(tariffRuleRepository.save(any(TariffRule.class))).thenReturn(existingRule);

        TariffRule result = tariffRuleService.updateTariffRule("C840", "C156", 1L, 100L, update);
        assertEquals(new BigDecimal("10"), result.getRate());
        assertEquals(Arrays.asList(new BigDecimal("10")), result.getAdditionalFees());

        // Verify the existingRule was updated with new values and saved
        verify(tariffRuleRepository).save(argThat(tariffRule
                -> tariffRule.getRate().equals(new BigDecimal("10"))
                && tariffRule.getAdditionalFees().equals(Arrays.asList(new BigDecimal("10")))
        ));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongTariffId() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(TariffRuleNotFoundException.class, ()
                -> tariffRuleService.updateTariffRule("C840", "C156", 1L, 200L, update));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_NullRate() {
        TariffRule update = new TariffRule();
        update.setRate(null);
        TariffRule result = tariffRuleService.updateTariffRule("C840", "C156", 1L, 100L, update);
        assertEquals(new BigDecimal("5"), result.getRate());
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongFromCountryCode() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(CountryNotFoundException.class, ()
                -> tariffRuleService.updateTariffRule("XX", "CN", 1L, 100L, update));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongToCountryCode() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(CountryNotFoundException.class, ()
                -> tariffRuleService.updateTariffRule("US", "XX", 1L, 100L, update));
    }

    @Test
    void testUpdateTariffRule_ByCodesAndIds_WrongProduct() {
        TariffRule update = new TariffRule();
        update.setRate(new BigDecimal("10"));
        assertThrows(ProductNotFoundException.class, ()
                -> tariffRuleService.updateTariffRule("C840", "C156", 2L, 100L, update));
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
    void testDeleteTariffRule_ByCodesAndIds_WrongFromCountryCode() {
        assertThrows(CountryNotFoundException.class, ()
                -> tariffRuleService.deleteTariffRule("XX", "CN", 1L, 100L));
    }

    @Test
    void testDeleteTariffRule_ByCodesAndIds_WrongToCountryCode() {
        assertThrows(CountryNotFoundException.class, ()
                -> tariffRuleService.deleteTariffRule("US", "XX", 1L, 100L));
    }

    @Test
    void testDeleteTariffRuleByProduct_NotFound2() {
        when(productRepository.findById(1L)).thenReturn(null);
        assertThrows(ProductNotFoundException.class, () -> tariffRuleService.deleteTariffRule("C840", "C156", 2L, 100L));
    }

    @Test
    void testDeleteTariffRule_ByCodesAndIds_Success_withListLookup() {
        TariffRule existingRule = new TariffRule();
        existingRule.setId(100L);
        existingRule.setFromCountry(fromCountry);
        existingRule.setToCountry(toCountry);
        existingRule.setProduct(product);
        existingRule.setRate(new BigDecimal("5"));

        Page<TariffRule> page = new PageImpl<>(List.of(existingRule));
        when(tariffRuleRepository.findByFromCountryCountryCodeAndToCountryCountryCodeAndProductId(eq("C840"), eq("C156"), eq(1L), any(Pageable.class)))
                .thenReturn(page);

        tariffRuleService.deleteTariffRule("C840", "C156", 1L, 100L);
        verify(tariffRuleRepository).delete(existingRule);
    }

    // ---------------- GET BY COUNTRY ----------------
    @Test
    void testGetTariffRulesByCountryCode_Success() {
        when(countryRepository.existsById("US")).thenReturn(true);
        Page<TariffRule> page = new PageImpl<>(Arrays.asList(tariffRule));
        when(tariffRuleRepository.findByFromCountryCountryCodeOrToCountryCountryCode(eq("US"), eq("US"), any(Pageable.class)))
                .thenReturn(page);

        Page<TariffRule> result = tariffRuleService.getTariffRulesByCountryCode("US", Pageable.unpaged());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetTariffRulesByCountryCode_CountryNotFound() {
        when(countryRepository.existsById("XX")).thenReturn(false);
        assertThrows(CountryNotFoundException.class, () -> tariffRuleService.getTariffRulesByCountryCode("XX", Pageable.unpaged()));
    }

    @Test
    void testGetTariffRulesByToCountryCode() {
        when(countryRepository.existsById("CN")).thenReturn(true);
        Page<TariffRule> page = new PageImpl<>(Arrays.asList(tariffRule));
        when(tariffRuleRepository.findByToCountryCountryCode(eq("CN"), any(Pageable.class))).thenReturn(page);

        Page<TariffRule> result = tariffRuleService.getTariffRulesByToCountryCode("CN", Pageable.unpaged());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetTariffRulesByProductId() {
        when(productRepository.existsById(1L)).thenReturn(true);
        Page<TariffRule> page = new PageImpl<>(Arrays.asList(tariffRule));
        when(tariffRuleRepository.findByProductId(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<TariffRule> result = tariffRuleService.getTariffRulesByProductId(1L, Pageable.unpaged());
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetTariffRulesByProductId_ProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductNotFoundException.class, () -> tariffRuleService.getTariffRulesByProductId(1L, Pageable.unpaged()));
    }

}
