package com.tariff.dto.response;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class TariffCalculationResponse {
    @Schema(description = "Tariff rate applied (%)")
    private final BigDecimal tariffRate;

    @Schema(description = "Total calculated tariff amount")
    private final BigDecimal calculatedTariff;
    
    @Schema(description = "Total amount from additional fees applied")
    private final BigDecimal totalAdditionalFees;
    
    private final String fromCountry;
    private final String toCountry;

    private final BigDecimal totalCost;
    private final String calculationType;

    @Schema(description = "List of additional fee rates applied (%)")
    private final List<BigDecimal> additionalFees;

    @Schema(description = "Total value of the import (unit cost * quantity/weight)")
    private final BigDecimal importValue;

    public TariffCalculationResponse(String fromCountry,String toCountry,BigDecimal tariffRate, BigDecimal calculatedTariff, BigDecimal totalCost, BigDecimal additionalFee,String calculationType, List<BigDecimal> additionalFees, BigDecimal importValue){
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
        this.totalAdditionalFees = additionalFee;
        this.totalCost = totalCost;
        this.additionalFees = additionalFees;
        this.calculationType = calculationType;
        this.importValue = importValue;
    }

    public BigDecimal getTariffRate() {
        return tariffRate;
    }

    public BigDecimal getCalculatedTariff() {
        return calculatedTariff;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public BigDecimal getTotalAdditionalFees() {
        return totalAdditionalFees;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public List<BigDecimal> getAdditionalFees() {
        return additionalFees;
    }
    public String getcalculationType() {
        return calculationType;
    }

    public BigDecimal getImportValue() {
        return importValue;
    }
}
