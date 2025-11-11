package com.tariff.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.dto.response.NewsApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    @Value("${news.api.key}")
    private String apiKey;

    @Value("${news.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public NewsService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get latest news articles
     */
    public NewsApiResponse getLatestNews(int size) {
        try {
            String url = buildUrl("latest", null, size);
            logger.info("Fetching latest news from: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(response, NewsApiResponse.class);
        } catch (Exception e) {
            logger.error("Error fetching latest news", e);
            throw new RuntimeException("Failed to fetch latest news", e);
        }
    }

    /**
     * Get tariff-related news
     */
    public NewsApiResponse getTariffNews(int page, int size) {
        try {
            // Broaden query to include common tariff-related terms for better relevance
            String query = "tariff";
            String url = buildUrl("search", query, size);
            logger.info("Fetching tariff news from: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(response, NewsApiResponse.class);
        } catch (Exception e) {
            logger.error("Error fetching tariff news", e);
            throw new RuntimeException("Failed to fetch tariff news", e);
        }
    }

    /**
     * Get trade-related news
     */
    public NewsApiResponse getTradeNews(int page, int size) {
        try {
            // Broaden query for international trade coverage
            String query = "trade";
            String url = buildUrl("search", query, size);
            logger.info("Fetching trade news from: {}", url);
            
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(response, NewsApiResponse.class);
        } catch (Exception e) {
            logger.error("Error fetching trade news", e);
            throw new RuntimeException("Failed to fetch trade news", e);
        }
    }

    /**
     * Search news by query
     */
    public NewsApiResponse searchNews(String query, int page, int size) {
        try {
            String url = buildUrl("search", query, size);
            logger.info("Searching news with query '{}' from: {}", query, url);
            
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(response, NewsApiResponse.class);
        } catch (Exception e) {
            logger.error("Error searching news with query: {}", query, e);
            throw new RuntimeException("Failed to search news", e);
        }
    }

    /**
     * Get news by country
     */
    public NewsApiResponse getNewsByCountry(String country, int page, int size) {
        try {
            String url = buildUrlWithCountry(country, size);
            logger.info("Fetching news for country '{}' from: {}", country, url);
            
            String response = restTemplate.getForObject(url, String.class);
            return objectMapper.readValue(response, NewsApiResponse.class);
        } catch (Exception e) {
            logger.error("Error fetching news for country: {}", country, e);
            throw new RuntimeException("Failed to fetch news for country", e);
        }
    }

    /**
     * Build URL for API requests
     */
    private String buildUrl(String endpoint, String query, int size) {
        StringBuilder url = new StringBuilder(apiUrl);
        url.append("?apikey=").append(apiKey);
        url.append("&language=en");
        url.append("&size=").append(size);
        
        // Add relevant categories for business/economics news to improve relevance
        // Valid newsdata.io categories: business, politics, world, etc.
        url.append("&category=business,politics");

        if (query != null && !query.trim().isEmpty()) {
            try {
                String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
                url.append("&q=").append(encodedQuery);
            } catch (UnsupportedEncodingException e) {
                logger.error("Error encoding query: {}", query, e);
                // Fallback without encoding
                url.append("&q=").append(query);
            }
        }

        return url.toString();
    }

    /**
     * Build URL for country-specific requests
     */
    private String buildUrlWithCountry(String country, int size) {
        StringBuilder url = new StringBuilder(apiUrl);
        url.append("?apikey=").append(apiKey);
        url.append("&language=en");
        url.append("&size=").append(size);
        url.append("&country=").append(country.toLowerCase());

        return url.toString();
    }
}