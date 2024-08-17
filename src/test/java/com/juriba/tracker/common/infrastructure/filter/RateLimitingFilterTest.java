package com.juriba.tracker.common.infrastructure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juriba.tracker.common.application.RateLimitingService;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RateLimitingFilterTest {

    @Mock
    private RateLimitingService rateLimitingService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private RateLimitingFilter rateLimitingFilter;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rateLimitingFilter = new RateLimitingFilter(rateLimitingService, objectMapper);
    }

    @Test
    void doFilterInternal_shouldAllowRequest_whenRateLimitNotExceeded() throws Exception {
        // Arrange
        ConsumptionProbe probe = mock(ConsumptionProbe.class);
        when(probe.isConsumed()).thenReturn(true);
        when(probe.getRemainingTokens()).thenReturn(5L);
        when(rateLimitingService.tryConsume(anyString())).thenReturn(probe);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        // Act
        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).addHeader("X-Rate-Limit-Remaining", "5");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_shouldBlockRequest_whenRateLimitExceeded() throws Exception {
        // Arrange
        ConsumptionProbe probe = mock(ConsumptionProbe.class);
        when(probe.isConsumed()).thenReturn(false);
        when(probe.getNanosToWaitForRefill()).thenReturn(1_000_000_000L); // 1 second
        when(rateLimitingService.tryConsume(anyString())).thenReturn(probe);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Act
        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(response).setContentType("application/json");
        verify(response).addHeader("X-Rate-Limit-Retry-After", "1");
        verify(filterChain, never()).doFilter(request, response);

        String responseBody = stringWriter.toString();
        assertTrue(responseBody.contains("Too Many Requests"));
        assertTrue(responseBody.contains("You have exceeded the rate limit"));
    }

    @Test
    void doFilterInternal_shouldUseXForwardedForHeader_whenPresent() throws Exception {
        // Arrange
        ConsumptionProbe probe = mock(ConsumptionProbe.class);
        when(probe.isConsumed()).thenReturn(true);
        when(rateLimitingService.tryConsume(anyString())).thenReturn(probe);
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.0.1");

        // Act
        rateLimitingFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(rateLimitingService).tryConsume("192.168.0.1");
    }
}