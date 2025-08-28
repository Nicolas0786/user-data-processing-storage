package com.example.userdataprocessingstorage.exception;

import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class UserFileExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException exception) {
        return ResponseEntity.badRequest().body(
                Map.of("error", "validation_error",
                        "message", exception.getMessage())
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIlegal(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(
                Map.of("error", "invalid_argument" ,
                        "message", exception.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handlaeGeneric(Exception exception) {
        return ResponseEntity.status(500).body(
                Map.of("error", "internal_error",
                        "message", exception.getMessage())
        );
    }

}
