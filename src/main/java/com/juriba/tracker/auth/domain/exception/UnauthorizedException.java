package com.juriba.tracker.auth.domain.exception;

import com.juriba.tracker.common.domain.exception.BaseException;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
