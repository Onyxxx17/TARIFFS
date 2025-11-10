package com.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.entity.TariffRule;
import com.tariff.service.TariffRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TariffRuleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TariffRuleService tariffRuleService;

    @InjectMocks
    private TariffRuleController tariffRuleController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private TariffRule tariffRule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tariffRuleController).build();

        tariffRule = new TariffRule();
        tariffRule.setId(1L);
        tariffRule.setRate(new BigDecimal("10"));
        tariffRule.setAdditionalFee(new BigDecimal("5"));
    }

    @Test
    void testGetAllTariffRules() throws Exception {
        when(tariffRuleService.listTariffRule()).thenReturn(List.of(tariffRule));

        mockMvc.perform(get("/api/tariff-rules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rate").value(10))
                .andExpect(jsonPath("$[0].additionalFee").value(5));

        verify(tariffRuleService).listTariffRule();
    }

    @Test
    void testGetTariffRuleById() throws Exception {
        when(tariffRuleService.getTariffRule(1L)).thenReturn(tariffRule);

        mockMvc.perform(get("/api/tariff-rules/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rate").value(10))
                .andExpect(jsonPath("$.additionalFee").value(5));

        verify(tariffRuleService).getTariffRule(1L);
    }

//     @Test
//     void testCreateTariffRuleWithCountryAndProduct() throws Exception {
//         TariffRule tariffRule = new TariffRule();
//         tariffRule.setRate(new BigDecimal("10"));
//         tariffRule.setId(1L);

//         when(tariffRuleService.addTariffRuleByCountriesAndProduct(eq("US"), eq("CN"), eq(1L), any(TariffRule.class)))
//                 .thenReturn(tariffRule);

//         mockMvc.perform(post("/api/tariff-rules/country/US/product/1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(tariffRule)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.rate").value(10));

//         verify(tariffRuleService).addTariffRuleByCountriesAndProduct(eq("US"), eq("CN"), eq(1L), any(TariffRule.class));
//     }
    
//     @Test
//     void createTariffRuleWithCountryAndProduct() throws Exception {
//         when(tariffRuleService.addTariffRule(any(TariffRule.class))).thenReturn(tariffRule);

//         mockMvc.perform(post("/api/tariff-rules")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(tariffRule)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(1))
//                 .andExpect(jsonPath("$.rate").value(10));

//         verify(tariffRuleService).addTariffRule(any(TariffRule.class));
//     }

    @Test
    void testUpdateTariffRule() throws Exception {
        when(tariffRuleService.updateTariffRule(eq(1L), any(TariffRule.class))).thenReturn(tariffRule);

        mockMvc.perform(put("/api/tariff-rules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tariffRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rate").value(10));

        verify(tariffRuleService).updateTariffRule(eq(1L), any(TariffRule.class));
    }

    @Test
    void testDeleteTariffRule() throws Exception {
        doNothing().when(tariffRuleService).deleteTariffRule(1L);

        mockMvc.perform(delete("/api/tariff-rules/1"))
                .andExpect(status().isOk());

        verify(tariffRuleService).deleteTariffRule(1L);
    }

    @Test
    void testCreateTariffRuleWithCountriesAndProduct() throws Exception {
        when(tariffRuleService.addTariffRuleByCountriesAndProduct(eq("US"), eq("SG"), eq(1L), any(TariffRule.class)))
                .thenReturn(tariffRule);

        mockMvc.perform(post("/api/tariff-rules/from-country/US/to-country/SG/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tariffRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(tariffRuleService).addTariffRuleByCountriesAndProduct(eq("US"), eq("SG"), eq(1L), any(TariffRule.class));
    }

    @Test
    void testUpdateTariffRuleWithCountriesAndProduct() throws Exception {
        when(tariffRuleService.updateTariffRule(eq("US"), eq("SG"), eq(1L), eq(1L), any(TariffRule.class)))
                .thenReturn(tariffRule);

        mockMvc.perform(put("/api/tariff-rules/from-country/US/to-country/SG/product/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tariffRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(tariffRuleService).updateTariffRule(eq("US"), eq("SG"), eq(1L), eq(1L), any(TariffRule.class));
    }

    @Test
    void testDeleteTariffRuleWithCountriesAndProduct() throws Exception {
        doNothing().when(tariffRuleService).deleteTariffRule("US", "SG", 1L, 1L);

        mockMvc.perform(delete("/api/tariff-rules/from-country/US/to-country/SG/product/1/1"))
                .andExpect(status().isOk());

        verify(tariffRuleService).deleteTariffRule("US", "SG", 1L, 1L);
    }

    @Test
    void testGetTariffRulesByCountry() throws Exception {
        when(tariffRuleService.getTariffRulesByCountryCode("US")).thenReturn(List.of(tariffRule));

        mockMvc.perform(get("/api/tariff-rules/country/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(tariffRuleService).getTariffRulesByCountryCode("US");
    }

    @Test
    void testGetTariffRulesByFromCountry() throws Exception {
        when(tariffRuleService.getTariffRulesByFromCountryCode("US")).thenReturn(List.of(tariffRule));

        mockMvc.perform(get("/api/tariff-rules/from-country/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(tariffRuleService).getTariffRulesByFromCountryCode("US");
    }

    @Test
    void testGetTariffRulesByToCountry() throws Exception {
        when(tariffRuleService.getTariffRulesByToCountryCode("SG")).thenReturn(List.of(tariffRule));

        mockMvc.perform(get("/api/tariff-rules/to-country/SG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(tariffRuleService).getTariffRulesByToCountryCode("SG");
    }

    @Test
    void testGetTariffRulesByProduct() throws Exception {
        when(tariffRuleService.getTariffRulesByProductId(1L)).thenReturn(List.of(tariffRule));

        mockMvc.perform(get("/api/tariff-rules/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(tariffRuleService).getTariffRulesByProductId(1L);
    }
}