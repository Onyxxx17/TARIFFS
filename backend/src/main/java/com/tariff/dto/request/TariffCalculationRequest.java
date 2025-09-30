package com.tariff.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public class TariffCalculationRequest {

    @Schema(description = "Exporter country ID (nullable for MFN tariffs)", example = "North Korea")
    private String fromCountry;

    @Schema(description = "Importer country ID", example = "United States of America")
    private String toCountry;

    @Schema(description = "Product ID", example = "10129")
    private Long productId;

    @Schema(description = "Unit cost of the product", example = "25.0")
    private BigDecimal unitCost;

    @Schema(description = "Quantity of product", example = "1000")
    private Integer quantity;

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

    public void setEffectiveYear(Integer effectiveYear) {
        this.effectiveYear = effectiveYear;
    }
}
