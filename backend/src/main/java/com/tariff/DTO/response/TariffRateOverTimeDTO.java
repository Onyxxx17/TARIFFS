package com.tariff.dto.response;

import java.math.BigDecimal;

public class TariffRateOverTimeDTO {
    private Integer year;
    private BigDecimal rate;

    public TariffRateOverTimeDTO() {}

    public TariffRateOverTimeDTO(Integer year, BigDecimal rate) {
        this.year = year;
        this.rate = rate;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
