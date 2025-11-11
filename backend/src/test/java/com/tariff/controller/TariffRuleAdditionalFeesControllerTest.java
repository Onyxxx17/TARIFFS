package com.tariff.controller;

import com.tariff.entity.TariffRule;
import com.tariff.service.TariffRuleAdditionalFeesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TariffRuleAdditionalFeesController.class)
public class TariffRuleAdditionalFeesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TariffRuleAdditionalFeesService additionalFeesService;

    @BeforeEach
    public void setUp() {
        // MockitoAnnotations.openMocks(this) is not needed with @MockBean
    }

    @Test
    @WithMockUser
    public void testGetAdditionalFees() throws Exception {
        when(additionalFeesService.getAdditionalFeesByTariffRuleId(1L)).thenReturn(Collections.singletonList(new BigDecimal("10.5")));

        mockMvc.perform(get("/api/tariff-rules/additional-fees/tariff-rule/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(10.5));
    }

    @Test
    @WithMockUser
    public void testAddAdditionalFee() throws Exception {
        mockMvc.perform(post("/api/tariff-rules/additional-fees/tariff-rule/1")
                        .param("feeRate", "12.0")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testRemoveAdditionalFee() throws Exception {
        mockMvc.perform(delete("/api/tariff-rules/additional-fees/tariff-rule/1")
                        .param("feeRate", "12.0")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testClearAllAdditionalFees() throws Exception {
        mockMvc.perform(delete("/api/tariff-rules/additional-fees/tariff-rule/1/clear")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetTariffRulesWithCarbonTax() throws Exception {
        when(additionalFeesService.getTariffRulesWithCarbonTax()).thenReturn(Collections.singletonList(mock(TariffRule.class)));

        mockMvc.perform(get("/api/tariff-rules/additional-fees/carbon-tax"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetTariffRulesWithSanitaryBarriers() throws Exception {
        when(additionalFeesService.getTariffRulesWithSanitaryBarriers()).thenReturn(Collections.singletonList(mock(TariffRule.class)));

        mockMvc.perform(get("/api/tariff-rules/additional-fees/sanitary-barriers"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetAllAdditionalFeesEntries() throws Exception {
        when(additionalFeesService.getAllAdditionalFeesEntries()).thenReturn(Collections.singletonList(Collections.singletonMap("key", "value")));

        mockMvc.perform(get("/api/tariff-rules/additional-fees/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetAllUniqueAdditionalFeeRates() throws Exception {
        when(additionalFeesService.getAllUniqueAdditionalFeeRates()).thenReturn(Collections.singletonList(new BigDecimal("15.0")));

        mockMvc.perform(get("/api/tariff-rules/additional-fees/unique-rates"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetAdditionalFeeUsageStatistics() throws Exception {
        when(additionalFeesService.getAdditionalFeeUsageStatistics()).thenReturn(Collections.singletonList(Collections.singletonMap("key", "value")));

        mockMvc.perform(get("/api/tariff-rules/additional-fees/statistics"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetAllAdditionalFeesWithDetails() throws Exception {
        when(additionalFeesService.getAllAdditionalFeesWithDetails()).thenReturn(Collections.singletonList(Collections.singletonMap("key", "value")));

        mockMvc.perform(get("/api/tariff-rules/additional-fees/detailed"))
                .andExpect(status().isOk());
    }
}
