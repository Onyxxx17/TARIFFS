package com.tariff.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.service.JwtBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom filter to enforce strict JWT token expiration
 */
public class JwtExpirationFilter extends OncePerRequestFilter {

    private final JwtBlacklistService jwtBlacklistService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtExpirationFilter(JwtBlacklistService jwtBlacklistService) {
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");

            // Check if token is blacklisted
            if (jwtBlacklistService.isBlacklisted(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Token has been invalidated\"}");
                response.setContentType("application/json");
                return;
            }

            // Check expiration directly from token
            try {
                String[] parts = token.split("\\.");
                if (parts.length == 3) {
                    String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
                    Map<String, Object> claims = objectMapper.readValue(payload, Map.class);

                    // Get expiration timestamp
                    if (claims.containsKey("exp")) {
                        long exp = ((Number) claims.get("exp")).longValue();
                        Instant expiration = Instant.ofEpochSecond(exp);

                        // Check if token is expired
                        if (expiration.isBefore(Instant.now())) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"error\": \"Token has expired\"}");
                            response.setContentType("application/json");
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                // If we can't parse the token, let Spring Security handle it
                logger.debug("Error parsing JWT token", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
