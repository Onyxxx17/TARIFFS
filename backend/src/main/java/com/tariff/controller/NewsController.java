package com.tariff.controller;

import com.tariff.dto.response.NewsApiResponse;
import com.tariff.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news")
@CrossOrigin(origins = "*")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * Get latest news articles
     */
    @GetMapping("/latest")
    public ResponseEntity<NewsApiResponse> getLatestNews(
            @RequestParam(defaultValue = "10") int size) {
        try {
            NewsApiResponse response = newsService.getLatestNews(size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to fetch latest news"));
        }
    }

    /**
     * Get tariff-related news
     */
    @GetMapping("/tariff")
    public ResponseEntity<NewsApiResponse> getTariffNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            NewsApiResponse response = newsService.getTariffNews(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to fetch tariff news"));
        }
    }

    /**
     * Get trade-related news
     */
    @GetMapping("/trade")
    public ResponseEntity<NewsApiResponse> getTradeNews(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            NewsApiResponse response = newsService.getTradeNews(page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to fetch trade news"));
        }
    }

    /**
     * Search news articles
     */
    @GetMapping("/search")
    public ResponseEntity<NewsApiResponse> searchNews(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (q == null || q.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Query parameter 'q' is required"));
            }
            
            NewsApiResponse response = newsService.searchNews(q, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to search news"));
        }
    }

    /**
     * Get news by country
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<NewsApiResponse> getNewsByCountry(
            @PathVariable String country,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            NewsApiResponse response = newsService.getNewsByCountry(country, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to fetch news for country"));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("News API is healthy");
    }

    /**
     * Create an error response
     */
    private NewsApiResponse createErrorResponse(String message) {
        NewsApiResponse errorResponse = new NewsApiResponse();
        errorResponse.setStatus("error");
        errorResponse.setTotalResults(0);
        return errorResponse;
    }
}
