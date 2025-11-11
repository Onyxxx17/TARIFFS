package com.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.dto.request.SaveCalculationRequest;
import com.tariff.entity.ImportRecord;
import com.tariff.entity.Product;
import com.tariff.service.ImportRecordService;
import com.tariff.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ImportRecordControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ImportRecordService importRecordService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ImportRecordController importRecordController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ImportRecord record1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(importRecordController).build();

        record1 = new ImportRecord();
        record1.setId(1L);
        record1.setValue(1000);
    }

    @Test
    void testGetAllImportRecords() throws Exception {
        List<ImportRecord> records = Arrays.asList(record1);
        when(importRecordService.listImportRecord()).thenReturn(records);

        mockMvc.perform(get("/api/import-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(importRecordService).listImportRecord();
    }

    @Test
    void testGetImportRecordById() throws Exception {
        when(importRecordService.getImportRecord(1L)).thenReturn(record1);

        mockMvc.perform(get("/api/import-records/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(importRecordService).getImportRecord(1L);
    }

    @Test
    void testGetImportRecordsByProduct() throws Exception {
        when(importRecordService.getImportRecordsByProductId(10L)).thenReturn(Arrays.asList(record1));

        mockMvc.perform(get("/api/import-records/product/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(importRecordService).getImportRecordsByProductId(10L);
    }

    @Test
    void testGetImportRecordsByUser() throws Exception {
        when(importRecordService.getImportRecordsByUserId(20L)).thenReturn(Arrays.asList(record1));

        mockMvc.perform(get("/api/import-records/user/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(importRecordService).getImportRecordsByUserId(20L);
    }

    @Test
    void testCreateImportRecord() throws Exception {
        when(importRecordService.addImportRecord(any(ImportRecord.class))).thenReturn(record1);

        mockMvc.perform(post("/api/import-records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(importRecordService).addImportRecord(any(ImportRecord.class));
    }

    @Test
    void testCreateImportRecordWithProductAndUser() throws Exception {
        when(importRecordService.addImportRecordByProductAndUser(eq(10L), eq(20L), any(ImportRecord.class))).thenReturn(record1);

        mockMvc.perform(post("/api/import-records/product/10/user/20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(importRecordService).addImportRecordByProductAndUser(eq(10L), eq(20L), any(ImportRecord.class));
    }

    @Test
    void testUpdateImportRecord() throws Exception {
        when(importRecordService.updateImportRecord(eq(1L), any(ImportRecord.class))).thenReturn(record1);

        mockMvc.perform(put("/api/import-records/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(importRecordService).updateImportRecord(eq(1L), any(ImportRecord.class));
    }

    @Test
    void testUpdateImportRecordWithProductAndUser() throws Exception {
        when(importRecordService.updateImportRecordByProductAndUser(eq(10L), eq(20L), eq(1L), any(ImportRecord.class))).thenReturn(record1);

        mockMvc.perform(put("/api/import-records/product/10/user/20/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(importRecordService).updateImportRecordByProductAndUser(eq(10L), eq(20L), eq(1L), any(ImportRecord.class));
    }

    @Test
    void testDeleteImportRecord() throws Exception {
        doNothing().when(importRecordService).deleteImportRecord(1L);

        mockMvc.perform(delete("/api/import-records/1"))
                .andExpect(status().isOk());

        verify(importRecordService).deleteImportRecord(1L);
    }

    @Test
    void testSaveCalculation_Success() throws Exception {
        SaveCalculationRequest request = new SaveCalculationRequest();
        request.setProductId(10L);
        request.setFromCountryId("US");
        request.setToCountryId("CN");

        Product product = new Product();
        product.setId(10L);
        product.setName("Test Product");

        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password");
        when(importRecordService.getUserIdFromAuthentication(auth)).thenReturn(1L);
        when(productService.getProduct(10L)).thenReturn(product);
        when(importRecordService.addImportRecordByProductAndUser(anyLong(), anyLong(), any(ImportRecord.class))).thenReturn(record1);
        when(importRecordService.addImportRecordByCountryPair(anyString(), anyString(), any(ImportRecord.class))).thenReturn(record1);

        mockMvc.perform(post("/api/import-records/save-calculation")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(importRecordService).getUserIdFromAuthentication(auth);
        verify(productService).getProduct(10L);
        verify(importRecordService).addImportRecordByProductAndUser(eq(10L), eq(1L), any(ImportRecord.class));
        verify(importRecordService).addImportRecordByCountryPair(eq("US"), eq("CN"), any(ImportRecord.class));
    }

    @Test
    void testSaveCalculation_Unauthenticated() throws Exception {
        SaveCalculationRequest request = new SaveCalculationRequest();

        mockMvc.perform(post("/api/import-records/save-calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not authenticated"));
    }


    @Test
    void testDeleteCalculationHistory_Success() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password");
        when(importRecordService.getUserIdFromAuthentication(auth)).thenReturn(1L);
        doNothing().when(importRecordService).deleteCalculationHistory(5L, 1L);

        mockMvc.perform(delete("/api/import-records/history/5").principal(auth))
                .andExpect(status().isOk());

        verify(importRecordService).deleteCalculationHistory(5L, 1L);
    }

    @Test
    void testDeleteImportRecordWithProductAndUser() throws Exception {
        doNothing().when(importRecordService).deleteImportRecordByProductAndUser(10L, 20L, 1L);

        mockMvc.perform(delete("/api/import-records/product/10/user/20/1"))
                .andExpect(status().isOk());

        verify(importRecordService).deleteImportRecordByProductAndUser(10L, 20L, 1L);
    }
}