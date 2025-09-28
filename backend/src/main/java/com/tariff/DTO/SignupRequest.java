package com.tariff.DTO;

public record SignupRequest(
    String username,    
    String email,
    String password,
    String role
) {}
