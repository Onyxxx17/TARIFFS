package com.tariff.dto.response;

import java.time.LocalDateTime;

public class ChatResponse {
    private String message;
    private LocalDateTime timestamp;
    private boolean isError;

    public ChatResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatResponse(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isError = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
