package com.tariff.dto.request;

import java.math.BigDecimal;

public class TariffCalculationRequest {
    private String fromCountryId;
    private String toCountryId;
    private Long productId;
    private BigDecimal unitCost;
    private Integer quantity;
    private Integer effectiveYear;

    public String getFromCountryId() {
        return fromCountryId;
    }

    public String getToCountryId() {
        return toCountryId;
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

    public void setFromCountryId(String fromCountryId) {
        this.fromCountryId = fromCountryId;
    }

    public void setToCountryId(String toCountryId) {
        this.toCountryId = toCountryId;
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

    public void setEffectiveYear(Integer effectiveYear){
        this.effectiveYear = effectiveYear;
    }
}
