package com.juriba.tracker.common.domain.exception;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
