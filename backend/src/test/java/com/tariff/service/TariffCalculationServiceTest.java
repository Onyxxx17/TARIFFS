package com.tariff.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tariff.dto.request.TariffCalculationRequest;
import com.tariff.dto.response.TariffCalculationResponse;
import com.tariff.entity.Country;
import com.tariff.entity.TariffRule;
import com.tariff.exception.TariffRuleNotFoundException;
import com.tariff.repository.CountryRepository;
import com.tariff.repository.TariffRuleRepository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TariffCalculationServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private TariffRuleRepository tariffRuleRepository;

    @InjectMocks
    private TariffCalculationService tariffCalculationService;

    private TariffCalculationRequest request;
    private Country fromCountry;
    private Country toCountry;
    private TariffRule tariffRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fromCountry = new Country();
        fromCountry.setCountryCode("US");
        fromCountry.setName("United States");

        toCountry = new Country();
        toCountry.setCountryCode("CN");
        toCountry.setName("China");

        tariffRule = new TariffRule();
        tariffRule.setRate(new BigDecimal("5.0")); // 5%

        request = new TariffCalculationRequest();
        request.setFromCountry("United States");
        request.setToCountry("China");
        request.setProductId(1L);
        request.setUnitCost(new BigDecimal("100"));
        request.setQuantity(10);
        request.setEffectiveYear(2025);
    }

    @Test
    void testCalculateTariff_Success() {
        when(countryRepository.findByName("United States")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findByName("China")).thenReturn(Optional.of(toCountry));
        when(tariffRuleRepository.findApplicableTariffRule("US", "CN", 1L, 2025))
            .thenReturn(tariffRule);

        TariffCalculationResponse response = tariffCalculationService.calculateTariff(request);

        BigDecimal expectedImportValue = request.getUnitCost().multiply(BigDecimal.valueOf(request.getQuantity())); // 100*10 = 1000
        BigDecimal expectedTariff = expectedImportValue.multiply(tariffRule.getRate()).divide(BigDecimal.valueOf(100)); // 1000*5/100=50
        BigDecimal expectedTotal = expectedImportValue.add(expectedTariff); // 1000+50=1050

        assertEquals("United States", response.getFromCountry());
        assertEquals("China", response.getToCountry());
        assertEquals(tariffRule.getRate(), response.getTariffRate());
        assertEquals(expectedTotal, response.getCalculatedTariff());
    }

    @Test
    void testCalculateTariff_FromCountryNotFound() {
        when(countryRepository.findByName("United States")).thenReturn(Optional.empty());
        when(countryRepository.findByName("China")).thenReturn(Optional.of(toCountry));

        Exception exception = assertThrows(RuntimeException.class, () -> tariffCalculationService.calculateTariff(request));
        assertEquals("Country not found", exception.getMessage());
    }

    @Test
    void testCalculateTariff_ToCountryNotFound() {
        when(countryRepository.findByName("United States")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findByName("China")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> tariffCalculationService.calculateTariff(request));
        assertEquals("Country not found", exception.getMessage());
    }

    @Test
    void testCalculateTariff_TariffRuleNotFound() {
        when(countryRepository.findByName("United States")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findByName("China")).thenReturn(Optional.of(toCountry));
        when(tariffRuleRepository.findApplicableTariffRule("US", "CN", 1L, 2025))
            .thenReturn(null);

        assertThrows(TariffRuleNotFoundException.class, () -> tariffCalculationService.calculateTariff(request));
    }
}
