package com.tariff.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
    @Schema(description = "Email of the user", example = "demo@mail.com")
    String email,
    @Schema(description = "Password of the user", example = "demo123")
    String password
) {}
