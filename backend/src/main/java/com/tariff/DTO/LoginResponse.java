package com.tariff.dto;

public class LoginResponse {

    private final String username;
    private final String token;
    private final String type = "Bearer";
    private final String refreshToken;
    private final String message;

    public LoginResponse(String username, String accessToken, String refreshToken) {
        this.username = username;
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.message = "Login Successful";
    }

    // Getters required for JSON serialization
    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getMessage() {
        return message;
    }
}
