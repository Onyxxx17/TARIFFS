package com.tariff.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404 Error
public class ImportRecordNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ImportRecordNotFoundException(Long id) {
        super("Could not find import record " + id);
    }
    
}