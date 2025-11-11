package com.tariff.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class NewsApiResponse {
    
    private String status;
    private Integer totalResults;
    private List<NewsArticleResponse> results;
    
    @JsonProperty("nextPage")
    private String nextPage;

    // Default constructor
    public NewsApiResponse() {}

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public List<NewsArticleResponse> getResults() {
        return results;
    }

    public void setResults(List<NewsArticleResponse> results) {
        this.results = results;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
