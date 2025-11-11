package com.tariff.service;

import com.tariff.entity.TariffRule;
import com.tariff.exception.TariffRuleNotFoundException;
import com.tariff.repository.TariffRuleAdditionalFeesRepository;
import com.tariff.repository.TariffRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TariffRuleAdditionalFeesServiceTest {

    @InjectMocks
    private TariffRuleAdditionalFeesService additionalFeesService;

    @Mock
    private TariffRuleAdditionalFeesRepository additionalFeesRepository;

    @Mock
    private TariffRuleRepository tariffRuleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAdditionalFeesByTariffRuleId_Success() {
        when(tariffRuleRepository.existsById(1L)).thenReturn(true);
        when(additionalFeesRepository.findAdditionalFeesByTariffRuleId(1L))
                .thenReturn(Arrays.asList(new BigDecimal("10.5"), new BigDecimal("5.0")));

        List<BigDecimal> fees = additionalFeesService.getAdditionalFeesByTariffRuleId(1L);

        assertNotNull(fees);
        assertEquals(2, fees.size());
    }

    @Test
    public void testGetAdditionalFeesByTariffRuleId_NotFound() {
        when(tariffRuleRepository.existsById(1L)).thenReturn(false);
        assertThrows(TariffRuleNotFoundException.class, () -> {
            additionalFeesService.getAdditionalFeesByTariffRuleId(1L);
        });
    }

    @Test
    public void testGetAllAdditionalFeesEntries() {
        Object[] result1 = {1L, new BigDecimal("10.5")};
        when(additionalFeesRepository.findAllAdditionalFeesEntries()).thenReturn(Collections.singletonList(result1));

        List<Map<String, Object>> entries = additionalFeesService.getAllAdditionalFeesEntries();

        assertNotNull(entries);
        assertEquals(1, entries.size());
        assertEquals(1L, entries.get(0).get("tariffRuleId"));
    }

    @Test
    public void testGetAllUniqueAdditionalFeeRates() {
        when(additionalFeesRepository.findAllUniqueAdditionalFeeRates())
                .thenReturn(Arrays.asList(new BigDecimal("10.5"), new BigDecimal("15.0")));

        List<BigDecimal> rates = additionalFeesService.getAllUniqueAdditionalFeeRates();

        assertNotNull(rates);
        assertEquals(2, rates.size());
    }

    @Test
    public void testGetAdditionalFeeUsageStatistics() {
        Object[] result1 = {new BigDecimal("10.5"), 5L};
        when(additionalFeesRepository.findAdditionalFeeUsageStatistics()).thenReturn(Collections.singletonList(result1));

        List<Map<String, Object>> stats = additionalFeesService.getAdditionalFeeUsageStatistics();

        assertNotNull(stats);
        assertEquals(1, stats.size());
        assertEquals(5L, stats.get(0).get("usageCount"));
    }

    @Test
    public void testGetAllAdditionalFeesWithDetails() {
        Object[] result1 = {1L, new BigDecimal("10.5"), new BigDecimal("2.5"), 2023, "USA", "CHN", "Laptop"};
        when(additionalFeesRepository.findAllAdditionalFeesWithDetails()).thenReturn(Collections.singletonList(result1));

        List<Map<String, Object>> details = additionalFeesService.getAllAdditionalFeesWithDetails();

        assertNotNull(details);
        assertEquals(1, details.size());
        assertEquals("Laptop", details.get(0).get("productName"));
    }

    @Test
    public void testAddAdditionalFeeToTariffRule_Success() {
        TariffRule rule = new TariffRule();
        rule.setAdditionalFees(new ArrayList<>());
        when(tariffRuleRepository.findById(1L)).thenReturn(Optional.of(rule));

        additionalFeesService.addAdditionalFeeToTariffRule(1L, new BigDecimal("12.0"));

        verify(tariffRuleRepository, times(1)).save(rule);
        assertTrue(rule.getAdditionalFees().contains(new BigDecimal("12.0")));
    }

    @Test
    public void testRemoveAdditionalFeeFromTariffRule_Success() {
        TariffRule rule = new TariffRule();
        rule.setAdditionalFees(new ArrayList<>(Collections.singletonList(new BigDecimal("12.0"))));
        when(tariffRuleRepository.findById(1L)).thenReturn(Optional.of(rule));

        additionalFeesService.removeAdditionalFeeFromTariffRule(1L, new BigDecimal("12.0"));

        verify(tariffRuleRepository, times(1)).save(rule);
        assertFalse(rule.getAdditionalFees().contains(new BigDecimal("12.0")));
    }

    @Test
    public void testClearAllAdditionalFeesForTariffRule_Success() {
        TariffRule rule = new TariffRule();
        rule.setAdditionalFees(new ArrayList<>(Collections.singletonList(new BigDecimal("12.0"))));
        when(tariffRuleRepository.findById(1L)).thenReturn(Optional.of(rule));

        additionalFeesService.clearAllAdditionalFeesForTariffRule(1L);

        verify(tariffRuleRepository, times(1)).save(rule);
        assertTrue(rule.getAdditionalFees().isEmpty());
    }

    @Test
    public void testGetTariffRulesWithCarbonTax() {
        when(additionalFeesRepository.findTariffRulesWithSpecificAdditionalFee(new BigDecimal("27.5")))
                .thenReturn(Collections.singletonList(new TariffRule()));

        List<TariffRule> rules = additionalFeesService.getTariffRulesWithCarbonTax();

        assertNotNull(rules);
        assertEquals(1, rules.size());
    }



    @Test
    public void testGetTariffRulesWithSanitaryBarriers() {
        when(additionalFeesRepository.findTariffRulesWithSpecificAdditionalFee(new BigDecimal("16.4")))
                .thenReturn(Collections.singletonList(new TariffRule()));

        List<TariffRule> rules = additionalFeesService.getTariffRulesWithSanitaryBarriers();

        assertNotNull(rules);
        assertEquals(1, rules.size());
    }
}
