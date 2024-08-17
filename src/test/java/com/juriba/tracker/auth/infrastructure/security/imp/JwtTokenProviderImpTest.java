package com.juriba.tracker.auth.infrastructure.security.imp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtTokenProviderImpTest {

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private Authentication authentication;

    private JwtTokenProviderImp jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProviderImp(jwtEncoder);
        // Set custom values for testing
        setField(jwtTokenProvider, "accessTokenExpiration", 3600L);
        setField(jwtTokenProvider, "refreshTokenExpiration", 86400L);
    }

    @Test
    void generateAccessToken_shouldCreateValidToken() {
        // Arrange
        when(authentication.getName()).thenReturn("user@example.com");
//        when(authentication.getAuthorities()).thenReturn(
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//        );
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked.access.token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        // Act
        String token = jwtTokenProvider.generateAccessToken(authentication);

        // Assert
        assertEquals("mocked.access.token", token);
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void generateRefreshToken_shouldCreateValidToken() {
        // Arrange
        when(authentication.getName()).thenReturn("user@example.com");
//        when(authentication.getAuthorities()).thenReturn(
//                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//        );
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn("mocked.refresh.token");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        // Act
        String token = jwtTokenProvider.generateRefreshToken(authentication);

        // Assert
        assertEquals("mocked.refresh.token", token);
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void getAccessTokenExpiration_shouldReturnCorrectExpiration() {
        // Act
        Instant expiration = jwtTokenProvider.getAccessTokenExpiration();

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.isAfter(Instant.now()));
        assertTrue(expiration.isBefore(Instant.now().plusSeconds(3601))); // 3600 seconds + 1 for margin
    }

    @Test
    void getRefreshTokenExpiration_shouldReturnCorrectExpiration() {
        // Act
        Instant expiration = jwtTokenProvider.getRefreshTokenExpiration();

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.isAfter(Instant.now()));
        assertTrue(expiration.isBefore(Instant.now().plusSeconds(86401))); // 86400 seconds + 1 for margin
    }

    // Helper method to set private fields for testing
    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Error setting field for testing", e);
        }
    }
}