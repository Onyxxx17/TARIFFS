package com.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.entity.TariffRule;
import com.tariff.service.TariffRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
        mockMvc = MockMvcBuilders.standaloneSetup(tariffRuleController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .alwaysDo(print())
                .build();

        tariffRule = new TariffRule(new BigDecimal("10"), 2024);
        tariffRule.setId(1L);
    }

    // ✅ Get all tariff rules
    @Test
    void testGetAllTariffRules() throws Exception {

        Page<TariffRule> page = new PageImpl<>(List.of(tariffRule));
        
        when(tariffRuleService.listTariffRule(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tariff-rules")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].rate").value(10.0))
                .andExpect(jsonPath("$.content[0].effectiveYear").value(2024));

        verify(tariffRuleService).listTariffRule(any(Pageable.class));
    }

    // ✅ Get tariff rule by ID
    @Test
    void testGetTariffRuleById() throws Exception {
        when(tariffRuleService.getTariffRule(1L)).thenReturn(tariffRule);

        mockMvc.perform(get("/api/tariff-rules/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rate").value(10));

        verify(tariffRuleService).getTariffRule(1L);
    }

    // ✅ Create tariff rule (simple)
    @Test
    void testCreateTariffRule() throws Exception {
        when(tariffRuleService.addTariffRule(any(TariffRule.class))).thenReturn(tariffRule);

        mockMvc.perform(post("/api/tariff-rules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariffRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rate").value(10));

        verify(tariffRuleService).addTariffRule(any(TariffRule.class));
    }

    // ✅ Update tariff rule (simple)
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

    // ✅ Delete tariff rule (simple)
    @Test
    void testDeleteTariffRule() throws Exception {
        doNothing().when(tariffRuleService).deleteTariffRule(1L);

        mockMvc.perform(delete("/api/tariff-rules/1"))
                .andExpect(status().isOk());

        verify(tariffRuleService).deleteTariffRule(1L);
    }

    // ✅ Create tariff rule with from/to countries and product
    @Test
    void testCreateTariffRuleWithCountriesAndProduct() throws Exception {
        when(tariffRuleService.addTariffRuleByCountriesAndProduct(eq("C840"), eq("C156"), eq(1L), any(TariffRule.class)))
                .thenReturn(tariffRule);

        mockMvc.perform(post("/api/tariff-rules/from-country/C840/to-country/C156/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariffRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(tariffRuleService).addTariffRuleByCountriesAndProduct(eq("C840"), eq("C156"), eq(1L), any(TariffRule.class));
    }

    // ✅ Update tariff rule with from/to countries and product
    @Test
    void testUpdateTariffRuleWithCountriesAndProduct() throws Exception {
        when(tariffRuleService.updateTariffRule(eq("C840"), eq("C156"), eq(1L), eq(1L), any(TariffRule.class)))
                .thenReturn(tariffRule);

        mockMvc.perform(put("/api/tariff-rules/from-country/C840/to-country/C156/product/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tariffRule)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(tariffRuleService).updateTariffRule(eq("C840"), eq("C156"), eq(1L), eq(1L), any(TariffRule.class));
    }

    // ✅ Delete tariff rule with from/to countries and product
    @Test
    void testDeleteTariffRuleWithCountriesAndProduct() throws Exception {
        doNothing().when(tariffRuleService).deleteTariffRule("C840", "C156", 1L, 1L);

        mockMvc.perform(delete("/api/tariff-rules/from-country/C840/to-country/C156/product/1/1"))
                .andExpect(status().isOk());

        verify(tariffRuleService).deleteTariffRule("C840", "C156", 1L, 1L);
    }

    // ✅ Get tariff rules by country
    @Test
    void testGetTariffRulesByCountry() throws Exception {
        Page<TariffRule> page = new PageImpl<>(List.of(tariffRule));
        when(tariffRuleService.getTariffRulesByCountryCode(eq("C840"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tariff-rules/country/C840"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(tariffRuleService).getTariffRulesByCountryCode(eq("C840"), any(Pageable.class));
    }

    // // ✅ Get tariff rules by from country
    // @Test
    // void testGetTariffRulesByFromCountry() throws Exception {
    //     Page<TariffRule> page = new PageImpl<>(List.of(tariffRule));
    //     when(tariffRuleService.getTariffRulesByFromCountryCode(eq("C840"), any(Pageable.class))).thenReturn(page);
    //     mockMvc.perform(get("/api/tariff-rules/from-country/C840"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.content[0].id").value(1));
    //     verify(tariffRuleService).getTariffRulesByFromCountryCode(eq("C840"), any(Pageable.class));
    // }
    // // ✅ Get tariff rules by to country
    // @Test
    // void testGetTariffRulesByToCountry() throws Exception {
    //     Page<TariffRule> page = new PageImpl<>(List.of(tariffRule));
    //     when(tariffRuleService.getTariffRulesByToCountryCode(eq("C840"), any(Pageable.class))).thenReturn(page);
    //     mockMvc.perform(get("/api/tariff-rules/to-country/C840"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.content[0].id").value(1));
    //     verify(tariffRuleService).getTariffRulesByToCountryCode(eq("C840"), any(Pageable.class));
    // }
    // ✅ Get tariff rules by product
    @Test
    void testGetTariffRulesByProduct() throws Exception {
        Page<TariffRule> page = new PageImpl<>(List.of(tariffRule));
        when(tariffRuleService.getTariffRulesByProductId(eq(11029L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tariff-rules/product/11029"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));

        verify(tariffRuleService).getTariffRulesByProductId(eq(11029L), any(Pageable.class));
    }
}
