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
        fromCountry.setCountryCode("C840");
        fromCountry.setName("United States of America");

        toCountry = new Country();
        toCountry.setCountryCode("C156");
        toCountry.setName("People's Republic of China");

        tariffRule = new TariffRule();
        tariffRule.setRate(new BigDecimal("5.0")); // 5%

        request = new TariffCalculationRequest();
        request.setFromCountry("United States of America");
        request.setToCountry("People's Republic of China");
        request.setProductId(1L);
        request.setUnitCost(new BigDecimal("100"));
        request.setQuantity(10);
        request.setEffectiveYear(2025);
    }

    @Test
    void testCalculateTariff_Success() {
        when(countryRepository.findByName("United States of America")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findByName("People's Republic of China")).thenReturn(Optional.of(toCountry));
        when(tariffRuleRepository.findApplicableTariffRule("C840", "C156", 1L, 2025))
                .thenReturn(tariffRule);

        TariffCalculationResponse response = tariffCalculationService.calculateTariff(request);

        BigDecimal expectedImportValue = request.getUnitCost().multiply(BigDecimal.valueOf(request.getQuantity())); // 100*10 = 1000
        BigDecimal expectedTariff = expectedImportValue.multiply(new BigDecimal("5.0")).divide(BigDecimal.valueOf(100)); // 1000*5/100=50
        BigDecimal expectedTotal = expectedImportValue.add(expectedTariff); // 1000+50=1050

        assertEquals("United States of America", response.getFromCountry());
        assertEquals("People's Republic of China", response.getToCountry());
        assertEquals(new BigDecimal("5.0"), response.getTariffRate());
        assertEquals(0, expectedTariff.compareTo(response.getCalculatedTariff())); // This should be just the tariff amount (50)
        assertEquals(0, expectedTotal.compareTo(response.getTotalCost())); // This should be the total cost (1050)
    }

    @Test
    void testCalculateTariff_FromCountryNotFound() {
        when(countryRepository.findByName("United States of America")).thenReturn(Optional.empty());
        when(countryRepository.findByName("People's Republic of China")).thenReturn(Optional.of(toCountry));

        Exception exception = assertThrows(RuntimeException.class, () -> tariffCalculationService.calculateTariff(request));
        assertEquals("Could not find country United States of America", exception.getMessage());
    }

    @Test
    void testCalculateTariff_ToCountryNotFound() {
        when(countryRepository.findByName("United States of America")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findByName("People's Republic of China")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> tariffCalculationService.calculateTariff(request));
        assertEquals("Could not find country People's Republic of China", exception.getMessage());
    }

    @Test
    void testCalculateTariff_TariffRuleNotFound() {
        when(countryRepository.findByName("United States of America")).thenReturn(Optional.of(fromCountry));
        when(countryRepository.findByName("People's Republic of China")).thenReturn(Optional.of(toCountry));
        when(tariffRuleRepository.findApplicableTariffRule("C840", "C156", 1L, 2025))
                .thenReturn(null);

        assertThrows(TariffRuleNotFoundException.class, () -> tariffCalculationService.calculateTariff(request));
    }
}
