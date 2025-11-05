package com.tariff.dto.request;

import java.math.BigDecimal;

public class TariffPredictionRequest {
    private String fromCountryCode;
    private String toCountryCode;
    private Long productId;
    private int predictedYear;

    public TariffPredictionRequest(){

    }

    public TariffPredictionRequest(String fromCountryCode, String toCountryCode, Long productId, int predictedYear) {
        this.fromCountryCode = fromCountryCode;
        this.toCountryCode = toCountryCode;
        this.productId = productId;
        this.predictedYear = predictedYear;
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

    public int getPredictedYear() {
        return predictedYear;
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

    public void setPredictedYear(int predictedYear) {
        this.predictedYear = predictedYear;
    }
}
