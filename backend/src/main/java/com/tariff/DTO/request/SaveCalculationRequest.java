package com.tariff.dto.request;

public class SaveCalculationRequest {

    private String fromCountryId;
    private String toCountryId;
    private Long productId;
    private double value;
    private int year;
    private double tariffRate;
    private double calculatedTariff;
    private double additionalFeeRate; // renamed from additionalFee to be more clear
    private double totalAdditionalFees; // new field for total additional fees amount
    private double totalCost;
    private String calculationType;

    public SaveCalculationRequest() {
    }

    public SaveCalculationRequest(String fromCountryId, String toCountryId, Long productId,
            double value, int year, double tariffRate, double calculatedTariff,
            double additionalFeeRate, double totalAdditionalFees, double totalCost) {
        this.fromCountryId = fromCountryId;
        this.toCountryId = toCountryId;
        this.productId = productId;
        this.value = value;
        this.year = year;
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
        this.additionalFeeRate = additionalFeeRate;
        this.totalAdditionalFees = totalAdditionalFees;
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

    public double getAdditionalFeeRate() {
        return additionalFeeRate;
    }

    public void setAdditionalFeeRate(double additionalFeeRate) {
        this.additionalFeeRate = additionalFeeRate;
    }

    public double getTotalAdditionalFees() {
        return totalAdditionalFees;
    }

    public void setTotalAdditionalFees(double totalAdditionalFees) {
        this.totalAdditionalFees = totalAdditionalFees;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }
}
