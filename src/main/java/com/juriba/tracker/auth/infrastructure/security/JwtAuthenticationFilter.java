package com.juriba.tracker.auth.infrastructure.security;

import com.juriba.tracker.auth.domain.exception.UnauthorizedException;
import com.juriba.tracker.auth.infrastructure.config.SecurityPathConfig;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final UserDetailsService userDetailsService;
    private final SecurityPathConfig securityPathConfig;

    public JwtAuthenticationFilter(JwtDecoder jwtDecoder, UserDetailsService userDetailsService, SecurityPathConfig securityPathConfig) {
        this.jwtDecoder = jwtDecoder;
        this.userDetailsService = userDetailsService;
        this.securityPathConfig = securityPathConfig;
    }

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request) {
        return securityPathConfig.publicPathsMatcher().matches(request);
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request,  HttpServletResponse response,
                                     FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwtDecoder.decode(jwt) != null) {
                String email = jwtDecoder.decode(jwt).getSubject();

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
//            log.error("Could not set uer authentication in security context. [Error message:{}] [Class name:{}]",ex.getMessage(), ex.getClass().getSimpleName());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new AuthenticationCredentialsNotFoundException("Authorization header is missing or invalid");
    }
}