package com.tariff.dto.response;

import com.tariff.entity.Country;

public class ImportRecordResponse {

    private Long id;
    private String fromCountryName;
    private String fromCountryCode;
    private String toCountryName;
    private String toCountryCode;
    private String productName;
    private Long productId;
    private double value;
    private int year;
    private double tariffRate;
    private double calculatedTariff;
    private double additionalFee;
    private double totalCost;

    public ImportRecordResponse() {
    }

    public ImportRecordResponse(Long id, String fromCountryName, String fromCountryCode,
            String toCountryName, String toCountryCode, String productName,
            Long productId, double value, int year, double tariffRate,
            double calculatedTariff, double additionalFee, double totalCost) {
        this.id = id;
        this.fromCountryName = fromCountryName;
        this.fromCountryCode = fromCountryCode;
        this.toCountryName = toCountryName;
        this.toCountryCode = toCountryCode;
        this.productName = productName;
        this.productId = productId;
        this.value = value;
        this.year = year;
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
        this.additionalFee = additionalFee;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromCountryName() {
        return fromCountryName;
    }

    public void setFromCountryName(String fromCountryName) {
        this.fromCountryName = fromCountryName;
    }

    public String getFromCountryCode() {
        return fromCountryCode;
    }

    public void setFromCountryCode(String fromCountryCode) {
        this.fromCountryCode = fromCountryCode;
    }

    public String getToCountryName() {
        return toCountryName;
    }

    public void setToCountryName(String toCountryName) {
        this.toCountryName = toCountryName;
    }

    public String getToCountryCode() {
        return toCountryCode;
    }

    public void setToCountryCode(String toCountryCode) {
        this.toCountryCode = toCountryCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getTariffRate() {
        return tariffRate;
    }

    public void setTariffRate(double tariffRate) {
        this.tariffRate = tariffRate;
    }

    public double getCalculatedTariff() {
        return calculatedTariff;
    }

    public void setCalculatedTariff(double calculatedTariff) {
        this.calculatedTariff = calculatedTariff;
    }

    public double getAdditionalFee() {
        return additionalFee;
    }

    public void setAdditionalFee(double additionalFee) {
        this.additionalFee = additionalFee;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
}
