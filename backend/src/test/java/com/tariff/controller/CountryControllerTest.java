package com.tariff.controller;

import com.tariff.entity.Country;
import com.tariff.service.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CountryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CountryService countryService;

    @InjectMocks
    private CountryController countryController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Country country1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();

        country1 = new Country();
        country1.setCountryCode("US");
        country1.setName("United States");
    }

    @Test
    void testGetAllCountries() throws Exception {
        List<Country> countries = Arrays.asList(country1);
        when(countryService.listCountry()).thenReturn(countries);

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].countryCode").value("US"))
                .andExpect(jsonPath("$[0].name").value("United States"));

        verify(countryService).listCountry();
    }

    @Test
    void testGetCountryByCode() throws Exception {
        when(countryService.getCountry("US")).thenReturn(country1);

        mockMvc.perform(get("/api/countries/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("US"))
                .andExpect(jsonPath("$.name").value("United States"));

        verify(countryService).getCountry("US");
    }

    @Test
    void testCreateCountry() throws Exception {
        when(countryService.addCountry(any(Country.class))).thenReturn(country1);

        mockMvc.perform(post("/api/countries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(country1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryCode").value("US"))
                .andExpect(jsonPath("$.name").value("United States"));

        verify(countryService).addCountry(any(Country.class));
    }

    @Test
    void testUpdateCountry() throws Exception {
        Country updatedCountry = new Country();
        updatedCountry.setCountryCode("US");
        updatedCountry.setName("USA");

        when(countryService.updateCountry(eq("US"), any(Country.class))).thenReturn(updatedCountry);

        mockMvc.perform(put("/api/countries/US")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCountry)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("USA"));

        verify(countryService).updateCountry(eq("US"), any(Country.class));
    }

    @Test
    void testDeleteCountry() throws Exception {
        doNothing().when(countryService).deleteCountry("US");

        mockMvc.perform(delete("/api/countries/US"))
                .andExpect(status().isOk());

        verify(countryService).deleteCountry("US");
    }
}
