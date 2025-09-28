package com.tariff.exception;

public class UserAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String email) {
        super("User with email " + email + " already exists");
    }

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}