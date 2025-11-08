package com.tariff.ai.dto;

import jakarta.validation.constraints.NotEmpty;

public record TariffAnalysisRequest(
    @NotEmpty String originCountry,
    @NotEmpty String destinationCountry,
    @NotEmpty String productDescription
) {}
