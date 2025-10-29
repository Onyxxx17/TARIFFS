package com.tariff.dto.request;

public class SaveCalculationRequest {

    private String fromCountryId;
    private String toCountryId;
    private Long productId;
    private double value;
    private int year;
    private double tariffRate;
    private double calculatedTariff;
    private double additionalFee;
    private double totalCost;

    public SaveCalculationRequest() {
    }

    public SaveCalculationRequest(String fromCountryId, String toCountryId, Long productId,
            double value, int year, double tariffRate, double calculatedTariff,
            double additionalFee, double totalCost) {
        this.fromCountryId = fromCountryId;
        this.toCountryId = toCountryId;
        this.productId = productId;
        this.value = value;
        this.year = year;
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
        this.additionalFee = additionalFee;
        this.totalCost = totalCost;
    }

    // Getters and Setters
    public String getFromCountryId() {
        return fromCountryId;
    }

    public void setFromCountryId(String fromCountryId) {
        this.fromCountryId = fromCountryId;
    }

    public String getToCountryId() {
        return toCountryId;
    }

    public void setToCountryId(String toCountryId) {
        this.toCountryId = toCountryId;
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
