package com.tariff.dto.response;

import java.util.List;

public class TariffComparisonDTO {
    private String country1Code;
    private String country1Name;
    private String country2Code;
    private String country2Name;
    private Long productId;
    private List<TariffRateOverTimeDTO> country1Rates; // Country1's tariff on imports from Country2
    private List<TariffRateOverTimeDTO> country2Rates; // Country2's tariff on imports from Country1

    public TariffComparisonDTO() {}

    public TariffComparisonDTO(String country1Code, String country1Name, 
                               String country2Code, String country2Name,
                               Long productId,
                               List<TariffRateOverTimeDTO> country1Rates, 
                               List<TariffRateOverTimeDTO> country2Rates) {
        this.country1Code = country1Code;
        this.country1Name = country1Name;
        this.country2Code = country2Code;
        this.country2Name = country2Name;
        this.productId = productId;
        this.country1Rates = country1Rates;
        this.country2Rates = country2Rates;
    }

    // Getters and Setters
    public String getCountry1Code() {
        return country1Code;
    }

    public void setCountry1Code(String country1Code) {
        this.country1Code = country1Code;
    }

    public String getCountry1Name() {
        return country1Name;
    }

    public void setCountry1Name(String country1Name) {
        this.country1Name = country1Name;
    }

    public String getCountry2Code() {
        return country2Code;
    }

    public void setCountry2Code(String country2Code) {
        this.country2Code = country2Code;
    }

    public String getCountry2Name() {
        return country2Name;
    }

    public void setCountry2Name(String country2Name) {
        this.country2Name = country2Name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public List<TariffRateOverTimeDTO> getCountry1Rates() {
        return country1Rates;
    }

    public void setCountry1Rates(List<TariffRateOverTimeDTO> country1Rates) {
        this.country1Rates = country1Rates;
    }

    public List<TariffRateOverTimeDTO> getCountry2Rates() {
        return country2Rates;
    }

    public void setCountry2Rates(List<TariffRateOverTimeDTO> country2Rates) {
        this.country2Rates = country2Rates;
    }
}
