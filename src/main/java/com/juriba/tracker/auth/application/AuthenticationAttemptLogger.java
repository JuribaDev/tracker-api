package com.juriba.tracker.auth.application;

public interface AuthenticationAttemptLogger {
    void logSuccessfulAttempt(String email);
    void logFailedAttempt(String email);
}
