package com.tariff.dto.response;

import java.math.BigDecimal;

public class TariffPredictionResponse {
    private String fromCountryCode;
    private String toCountryCode;
    private Long productId;
    private int predictedYear;
    private BigDecimal predictedRate;
    private double modelFit;



    public TariffPredictionResponse(
        String fromCountryCode,
        String toCountryCode,
        Long productId,
        int predictedYear,
        BigDecimal predictedRate,
        double modelFit
    ) {
        this.fromCountryCode = fromCountryCode;
        this.toCountryCode = toCountryCode;
        this.productId = productId;
        this.predictedYear = predictedYear;
        this.predictedRate = predictedRate;
        this.modelFit = modelFit;
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

    public BigDecimal getPredictedRate() {
        return predictedRate;
    }

    public double getModelFit() {
        return modelFit;
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

    public void setPredictedRate(BigDecimal predictedRate) {
        this.predictedRate = predictedRate;
    }

    public void setModelFit(double modelFit) {
        this.modelFit = modelFit;
    } 
}
