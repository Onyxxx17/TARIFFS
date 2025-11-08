package com.tariff.ai.dto;

import java.util.List;

public record TariffAnalysisResponse(
    String tariffRate,
    List<Fee> additionalFees,
    String summary,
    String disclaimer
) {
    public record Fee(String feeName, String amount, String description) {}
}
