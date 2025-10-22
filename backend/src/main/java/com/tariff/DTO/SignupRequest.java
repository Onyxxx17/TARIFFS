package com.tariff.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record SignupRequest(
    @Schema(description = "Username of the new user", example = "demoUser")
    String username,
    @Schema(description = "Email of the new user", example = "demo@mail.com")
    String email,
    @Schema(description = "Password of the new user", example = "demo123")
    String password,
    @Schema(description = "Role", example = "user")
    String role
) {}
