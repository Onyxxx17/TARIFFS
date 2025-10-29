package com.tariff.dto.request;

import java.math.BigDecimal;

public class TariffPredictionRequest {
    private String fromCountryCode;
    private String toCountryCode;
    private Long productId;
    private int year;

    public TariffPredictionRequest(){

    }

    public TariffPredictionRequest(String fromCountryCode, String toCountryCode, Long productId, int year) {
        this.fromCountryCode = fromCountryCode;
        this.toCountryCode = toCountryCode;
        this.productId = productId;
        this.year = year;
    }

    public String getFromCountryCode() {
        return fromCountryCode;
    }

    public String getToCountryCode() {
        return toCountryCode;
    }

    public Long getProductId() {
        return productId;
    }

    public int getYear() {
        return year;
    }

    public void setFromCountryCode(String fromCountryCode) {
        this.fromCountryCode = fromCountryCode;
    }

    public void setToCountryCode(String toCountryCode) {
        this.toCountryCode = toCountryCode;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
