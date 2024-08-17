package com.juriba.tracker.auth.infrastructure.security;

import com.juriba.tracker.auth.infrastructure.config.SecurityPathConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtDecoder jwtDecoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private SecurityPathConfig securityPathConfig;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtDecoder, userDetailsService, securityPathConfig);
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_shouldAuthenticate_whenValidToken() throws Exception {
        // Arrange
        String token = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(securityPathConfig.publicPathsMatcher()).thenReturn(request -> false);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn("user@example.com");
        when(jwtDecoder.decode(token)).thenReturn(jwt);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        verify(userDetailsService).loadUserByUsername("user@example.com");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenInvalidToken() throws Exception {
        // Arrange
        String token = "invalid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(securityPathConfig.publicPathsMatcher()).thenReturn(request -> false);
        when(jwtDecoder.decode(token)).thenThrow(new RuntimeException("Invalid token"));

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtDecoder).decode(token);
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldSkipAuthentication_forPublicPaths() throws Exception {
        // Arrange
        when(securityPathConfig.publicPathsMatcher()).thenReturn(request -> true);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtDecoder, never()).decode(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldNotAuthenticate_whenNoAuthorizationHeader() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);
        when(securityPathConfig.publicPathsMatcher()).thenReturn(request -> false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtDecoder, never()).decode(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
        verify(filterChain).doFilter(request, response);
    }
}