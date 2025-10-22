package com.tariff.dto;

public class SignupResponse {

    private final String username;
    private final String message;

    public SignupResponse(String username) {
        this.username = username;
        this.message = "User Created Successfully";
    }

    // Getters required for JSON serialization
    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }
}
