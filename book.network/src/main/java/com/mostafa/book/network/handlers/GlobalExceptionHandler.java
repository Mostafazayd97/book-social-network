package com.mostafa.book.network.handlers;


import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.mostafa.book.network.handlers.ErrorsCode.LOCKED_ACCOUNT;
import static com.mostafa.book.network.handlers.ErrorsCode.VALIDATION_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleLockedException(LockedException exception) {
        return ResponseEntity.status(403).body(
                ExceptionResponse.builder().code(LOCKED_ACCOUNT.getCode()).message(LOCKED_ACCOUNT.getDescription()).build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException exception) {
        Set<String> validationErrors = new HashSet<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            validationErrors.add(fieldError.getField() + " : " + fieldError.getDefaultMessage());
        });

        return ResponseEntity.status(400).body(
                ExceptionResponse.builder().code(VALIDATION_ERROR.getCode()).message(VALIDATION_ERROR.getDescription()).validationErrors(validationErrors).build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(500).body(
                ExceptionResponse.builder().code(500).build()
        );
    }
}
