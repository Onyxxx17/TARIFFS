package com.tariff.dto.request;

import java.math.BigDecimal;

public class TariffCalculationRequest {

    private String fromCountry;
    private String toCountry;
    private Long productId;
    private BigDecimal unitCost;
    private Integer quantity;
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
