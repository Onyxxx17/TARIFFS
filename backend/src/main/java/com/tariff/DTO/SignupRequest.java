package com.tariff.dto;

public record SignupRequest(
    String username,    
    String email,
    String password,
    String role
) {}
