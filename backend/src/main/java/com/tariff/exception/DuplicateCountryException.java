package com.tariff.exception;

public class DuplicateCountryException extends RuntimeException {
    
    public DuplicateCountryException(String message) {
        super(message);
    }
}