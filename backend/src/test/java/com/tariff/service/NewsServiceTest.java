package com.tariff.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.dto.response.NewsApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private final String testApiKey = "test-api-key";
    private final String testApiUrl = "http://test.api/v1/";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(newsService, "apiKey", testApiKey);
        ReflectionTestUtils.setField(newsService, "apiUrl", testApiUrl);
    }

    

    @Test
    void testBuildUrl_WithoutQuery() throws Exception {
        // Given
        Method method = NewsService.class.getDeclaredMethod("buildUrl", String.class, String.class, int.class);
        method.setAccessible(true);
        String endpoint = "latest";
        int size = 5;

        // When
        String url = (String) method.invoke(newsService, endpoint, null, size);

        // Then
        assertTrue(url.contains("apikey=" + testApiKey));
        assertTrue(url.contains("language=en"));
        assertTrue(url.contains("size=5"));
        assertFalse(url.contains("&q="));
    }

    @Test
    void testBuildUrlWithCountry() throws Exception {
        // Given
        Method method = NewsService.class.getDeclaredMethod("buildUrlWithCountry", String.class, int.class);
        method.setAccessible(true);
        String country = "US";
        int size = 10;

        // When
        String url = (String) method.invoke(newsService, country, size);

        // Then
        assertTrue(url.contains("apikey=" + testApiKey));
        assertTrue(url.contains("language=en"));
        assertTrue(url.contains("size=10"));
        assertTrue(url.contains("&country=us"));
    }
}
