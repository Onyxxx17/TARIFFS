package com.tariff.dto.response;

import java.math.BigDecimal;

public class TariffCalculationResponse {
    private final BigDecimal tariffRate;
    private final BigDecimal calculatedTariff;

    public TariffCalculationResponse(BigDecimal tariffRate, BigDecimal calculatedTariff){
        this.tariffRate = tariffRate;
        this.calculatedTariff = calculatedTariff;
    }

    public BigDecimal getTariffRate() {
        return tariffRate;
    }

    public BigDecimal getCalculatedTariff() {
        return calculatedTariff;
    }
}
