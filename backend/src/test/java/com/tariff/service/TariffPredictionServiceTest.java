package com.tariff.service;

import com.tariff.dto.request.TariffPredictionRequest;
import com.tariff.dto.response.TariffPredictionResponse;
import com.tariff.entity.Country;
import com.tariff.entity.Product;
import com.tariff.entity.TariffRule;
import com.tariff.repository.TariffRuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TariffPredictionServiceTest {

    @InjectMocks
    private TariffPredictionService predictionService;

    @Mock
    private TariffRuleRepository tariffRuleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPredictFutureTariffRate_Success() {
        TariffPredictionRequest request = new TariffPredictionRequest("US", "CN", 1L, 2025);

        Country fromCountry = new Country("US", "United States");
        Product product = new Product();
        product.setId(1L);

        TariffRule rule2020 = new TariffRule();
        rule2020.setFromCountry(fromCountry);
        rule2020.setEffectiveYear(2020);
        rule2020.setRate(new BigDecimal("5.0"));

        TariffRule rule2021 = new TariffRule();
        rule2021.setFromCountry(fromCountry);
        rule2021.setEffectiveYear(2021);
        rule2021.setRate(new BigDecimal("5.5"));

        when(tariffRuleRepository.findByToCountryAndProduct("CN", 1L)).thenReturn(Arrays.asList(rule2020, rule2021));

        TariffPredictionResponse response = predictionService.predictFutureTariffRate(request);

        assertNotNull(response);
        assertEquals(2025, response.getPredictedYear());
        assertTrue(response.getPredictedRate().doubleValue() > 0);
        assertEquals(1.0, response.getModelFit(), 0.001); // Perfect fit for linear data
    }

    @Test
    public void testPredictFutureTariffRate_NoData() {
        TariffPredictionRequest request = new TariffPredictionRequest("US", "CN", 1L, 2025);
        when(tariffRuleRepository.findByToCountryAndProduct("CN", 1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            predictionService.predictFutureTariffRate(request);
        });

        assertEquals("No tariff data found for product 1 to country CN", exception.getMessage());
    }

    @Test
    public void testPredictFutureTariffRate_NotEnoughData() {
        TariffPredictionRequest request = new TariffPredictionRequest("US", "CN", 1L, 2025);
        TariffRule rule2020 = new TariffRule();
        rule2020.setEffectiveYear(2020);
        rule2020.setRate(new BigDecimal("5.0"));

        when(tariffRuleRepository.findByToCountryAndProduct("CN", 1L)).thenReturn(Collections.singletonList(rule2020));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            predictionService.predictFutureTariffRate(request);
        });

        assertEquals("Not enough historical data to predict future rates.", exception.getMessage());
    }
}
