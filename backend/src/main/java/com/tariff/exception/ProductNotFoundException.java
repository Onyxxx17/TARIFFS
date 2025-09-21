package com.tariff.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class ProductNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ProductNotFoundException(Long id) {
        super("Could not find product " + id);
    }
    
}

