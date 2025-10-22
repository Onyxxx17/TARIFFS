package com.tariff.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tariff.service.JwtBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter to check if the JWT token is blacklisted This filter runs before the
 * JWT token is processed by Spring Security
 */
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final JwtBlacklistService jwtBlacklistService;

    public JwtBlacklistFilter(JwtBlacklistService jwtBlacklistService) {
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the token from the Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");

            // Check if the token is blacklisted
            if (jwtBlacklistService.isBlacklisted(token)) {
                // Clear the security context and return 401 Unauthorized
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Token has been invalidated\"}");
                return;
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);

        // After the request is processed, check if we have a JWT authentication
        // This helps us track which tokens are in use
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            // Optional: You could track active tokens here
        }
    }
}
