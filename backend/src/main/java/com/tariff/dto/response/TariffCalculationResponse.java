package com.tariff.dto.response;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public class TariffCalculationResponse {
    @Schema(description = "Tariff rate applied (%)")
    private final BigDecimal tariffRate;

    @Schema(description = "Tariff rate applied (%)")
    private final BigDecimal calculatedTariff;
    
    private final String fromCountry;
    private final String toCountry;

    private final BigDecimal totalCost;

    public TariffCalculationResponse(String fromCountry,String toCountry,BigDecimal tariffRate, BigDecimal calculatedTariff, BigDecimal totalCost){
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
        this.totalCost = totalCost;
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

    public BigDecimal getTotalCost() {
        return totalCost;
    }

}
