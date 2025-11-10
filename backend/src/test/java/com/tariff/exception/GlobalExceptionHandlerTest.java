package com.tariff.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
        webRequest = new ServletWebRequest(request);
    }

    @Test
    void handleDuplicateCountryException_returnsConflict() {
        DuplicateCountryException ex = new DuplicateCountryException("Country already exists");

        ResponseEntity<Map<String, Object>> response = handler.handleDuplicateCountryException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody().get("error"));
        assertEquals("Country already exists", response.getBody().get("message"));
        assertEquals("/api/test", response.getBody().get("path"));
    }

    @Test
    void handleCountryNotFoundException_returnsNotFound() {
        CountryNotFoundException ex = new CountryNotFoundException("US");

        ResponseEntity<Map<String, Object>> response = handler.handleCountryNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals("Could not find country US", response.getBody().get("message"));
        assertEquals("/api/test", response.getBody().get("path"));
    }

    @Test
    void handleIllegalArgumentException_returnsBadRequest() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");

        ResponseEntity<Map<String, Object>> response = handler.handleIllegalArgumentException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().get("error"));
        assertEquals("Invalid argument", response.getBody().get("message"));
        assertEquals("/api/test", response.getBody().get("path"));
    }

    @Test
    void handleRuntimeException_returnsInternalServerError() {
        RuntimeException ex = new RuntimeException("Something went wrong");

        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().get("error"));
        assertEquals("An unexpected error occurred", response.getBody().get("message"));
        assertEquals("/api/test", response.getBody().get("path"));
    }

    @Test
    void handleTariffRuleNotFoundException_returnsNotFound() {
        TariffRuleNotFoundException ex = new TariffRuleNotFoundException("US", "CN", 2025, 1L);

        ResponseEntity<Map<String, Object>> response = handler.handleTariffRuleNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().get("error"));
        assertEquals(ex.getMessage(), response.getBody().get("message"));
        assertEquals("/api/test", response.getBody().get("path"));
    }
}
