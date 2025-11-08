package com.tariff.ai.dto;

import jakarta.validation.constraints.NotEmpty;

public record TariffQueryRequest(
    @NotEmpty String query,
    String queryType // "analysis", "data", or "general"
) {}