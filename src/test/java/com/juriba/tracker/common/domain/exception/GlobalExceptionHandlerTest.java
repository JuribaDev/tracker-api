package com.juriba.tracker.common.domain.exception;

import com.juriba.tracker.auth.domain.exception.UnauthorizedException;
import com.juriba.tracker.common.presentation.dto.ErrorResponse;
import com.juriba.tracker.common.presentation.dto.ValidationErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("test-request");
    }

    @Test
    void handleMethodArgumentNotValid_shouldReturnBadRequest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("objectName", "field", "Error message"));
        when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(ex.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);
        ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(ex, null, HttpStatus.BAD_REQUEST, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof ValidationErrorResponse);
        ValidationErrorResponse errorResponse = (ValidationErrorResponse) response.getBody();
        assertEquals(1, errorResponse.getErrors().size());
        assertEquals("Error message", errorResponse.getErrors().get("field"));
    }

    @Test
    void handleNotFoundException_shouldReturnNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFoundException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorizedException_shouldReturnUnauthorized() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized access");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnauthorizedException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized access", response.getBody().getMessage());
    }

    @Test
    void handleBadCredentialsException_shouldReturnUnauthorized() {
        BadCredentialsException ex = new BadCredentialsException("Invalid credentials");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadCredentialsException(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody().getMessage());
    }

    @Test
    void handleAllUncaughtException_shouldReturnInternalServerError() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAllUncaughtException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }
}