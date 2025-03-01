package com.mostafa.book.network.handlers;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorsCode {
    INVALID_TOKEN(1000, "Invalid token", HttpStatus.UNAUTHORIZED),
    LOCKED_ACCOUNT(1000, "Locked account", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1001, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTS(1002, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "User not found", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(1004, "Validation error", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(5000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;
    @Getter
    private final Integer code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    ErrorsCode(Integer code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
