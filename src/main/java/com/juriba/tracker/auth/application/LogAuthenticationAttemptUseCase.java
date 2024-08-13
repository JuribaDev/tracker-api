package com.juriba.tracker.auth.application;

public interface LogAuthenticationAttemptUseCase {
    void execute(String email, boolean isSuccessful);
}
