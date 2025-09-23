package com.tariff.dto.response;

import java.math.BigDecimal;

public class TariffCalculationResponse {
    private final BigDecimal tariffRate;
    private final BigDecimal calculatedTariff;
    private final String fromCountry;
    private final String toCountry;
    public TariffCalculationResponse(String fromCountry,String toCountry,BigDecimal tariffRate, BigDecimal calculatedTariff){
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
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
}
