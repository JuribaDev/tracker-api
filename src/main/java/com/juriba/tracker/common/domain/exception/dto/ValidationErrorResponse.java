package com.juriba.tracker.common.domain.exception.dto;

import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Map;


@EqualsAndHashCode(callSuper = true)
public class ValidationErrorResponse extends ErrorResponse{
    private final Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, String path, LocalDateTime timestamp, Map<String, String> errors) {
        super(status, message, path, timestamp);
        this.errors = errors;
    }
}
