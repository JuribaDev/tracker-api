package com.juriba.tracker.common.domain.exception;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
