package com.tariff.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public class TariffCalculationRequest {


    @Schema(description = "Exporter country name (nullable for MFN tariffs)", example = "North Korea")
    private String fromCountry;

    @Schema(description = "Importer country name", example = "United States of America")
    private String toCountry;

    @Schema(description = "Product ID", example = "10129")
    private Long productId;

    @Schema(description = "Unit cost per quantity or per kilogram", example = "25.0")
    private BigDecimal unitCost;

    @Schema(description = "Quantity of the product (required if 'calculationType' is QUANTITY)", example = "1000")
    private Integer quantity;

    @Schema(description = "Weight of the product in kilograms (required if 'calculationType' is WEIGHT)", example = "500.0")
    private BigDecimal weight;

    @Schema(description = "Specify whether the tariff should be calculated by QUANTITY or WEIGHT", example = "QUANTITY", allowableValues = {"QUANTITY", "WEIGHT"})
    private String calculationType;

    @Schema(description = "Year for which tariff is applied", example = "2025")
    private Integer effectiveYear;


    public String getFromCountry() {
        return fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getWeight() { 
        return weight; 
    }

    public String getCalculationType() { 
        return calculationType; 
    }

    public Integer getEffectiveYear() {
        return effectiveYear;
    }

    public void setFromCountry(String fromCountryId) {
        this.fromCountry = fromCountryId;
    }

    public void setToCountry(String toCountryId) {
        this.toCountry = toCountryId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }
    
    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public void setEffectiveYear(Integer effectiveYear) {
        this.effectiveYear = effectiveYear;
    }
}
