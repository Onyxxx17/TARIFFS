package com.tariff.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle EntityNotFoundException (JPA built-in exception)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {
        return createErrorResponse("Resource not found", HttpStatus.NOT_FOUND, request);
    }

    // Handle NoSuchElementException (when Optional.get() fails)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, Object>> handleNoSuchElement(
            NoSuchElementException ex, WebRequest request) {
        return createErrorResponse("Resource not found", HttpStatus.NOT_FOUND, request);
    }

    // Handle EmptyResultDataAccessException (when trying to delete non-existent entity)
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyResultDataAccess(
            EmptyResultDataAccessException ex, WebRequest request) {
        return createErrorResponse("Resource not found for deletion", HttpStatus.NOT_FOUND, request);
    }

    // Handle DataIntegrityViolationException (database constraints, duplicates)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {
        String message = "Data integrity violation";
        if (ex.getMessage().contains("Duplicate entry")) {
            message = "Resource already exists";
        }
        return createErrorResponse(message, HttpStatus.CONFLICT, request);
    }

    // Handle MethodArgumentNotValidException (validation errors)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        return createErrorResponse("Invalid input data", HttpStatus.BAD_REQUEST, request);
    }

    // Handle HttpMessageNotReadableException (malformed JSON)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {
        return createErrorResponse("Malformed JSON request", HttpStatus.BAD_REQUEST, request);
    }

    // Handle HttpRequestMethodNotSupportedException (wrong HTTP method)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return createErrorResponse("HTTP method not supported", HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    // Handle IllegalArgumentException (invalid arguments)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        return createErrorResponse("Invalid argument: " + ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    // Handle NullPointerException
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Map<String, Object>> handleNullPointer(
            NullPointerException ex, WebRequest request) {
        return createErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Handle any other general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(
            Exception ex, WebRequest request) {
        // For debugging - you can remove this in production
        System.err.println("Unhandled exception: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
        
        return createErrorResponse("An unexpected error occurred", 
                                 HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    // Helper method to create consistent error responses
    private ResponseEntity<Map<String, Object>> createErrorResponse(
            String message, HttpStatus status, WebRequest request) {
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);
        errorDetails.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(errorDetails, status);
    }
}
