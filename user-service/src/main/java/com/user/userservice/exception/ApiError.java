package com.user.userservice.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private final String message;

    private final int status;

    private final String path;

    private final LocalDateTime timestamp;

    public ApiError(String message, int status, String path) {
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}