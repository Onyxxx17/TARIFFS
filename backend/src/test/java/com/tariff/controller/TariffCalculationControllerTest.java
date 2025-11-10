package com.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.dto.request.TariffCalculationRequest;
import com.tariff.dto.response.TariffCalculationResponse;
import com.tariff.service.TariffCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TariffCalculationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TariffCalculationService tariffCalculationService;

    @InjectMocks
    private TariffCalculationController tariffCalculationController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TariffCalculationRequest request;
    private TariffCalculationResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tariffCalculationController).build();

        request = new TariffCalculationRequest();
        request.setFromCountry("United States");
        request.setToCountry("China");
        request.setProductId(1L);
        request.setUnitCost(new BigDecimal("100"));
        request.setQuantity(5);
        request.setEffectiveYear(2025);

        response = new TariffCalculationResponse(
                "United States", // fromCountry
                "China", // toCountry
                new BigDecimal("10"), // tariffRate
                new BigDecimal("50"), // calculatedTariff
                new BigDecimal("550"), // totalCost
                new BigDecimal("5"), // totalAdditionalFees
                "standard", // calculationType
                Arrays.asList(new BigDecimal("5")), // additionalFees
                new BigDecimal("500") // importValue
        );
    }

    @Test
    void testCalculateTariff() throws Exception {
        when(tariffCalculationService.calculateTariff(any(TariffCalculationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/tariffs/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCountry").value("United States"))
                .andExpect(jsonPath("$.toCountry").value("China"))
                .andExpect(jsonPath("$.tariffRate").value(10))
                .andExpect(jsonPath("$.calculatedTariff").value(50));

        verify(tariffCalculationService).calculateTariff(any(TariffCalculationRequest.class));
    }
}
