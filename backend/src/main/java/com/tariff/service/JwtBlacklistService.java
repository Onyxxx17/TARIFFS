package com.tariff.service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service to manage JWT token blacklisting This helps with invalidating tokens
 * before their natural expiration
 */
@Service
public class JwtBlacklistService {

    // In-memory store of blacklisted tokens with their expiration time
    // In a production environment, this should be replaced with a Redis cache or similar
    private final Map<String, Instant> blacklistedTokens = new ConcurrentHashMap<>();

    /**
     * Add a token to the blacklist
     *
     * @param token The JWT token to blacklist
     * @param expiryTime When the token naturally expires
     */
    public void blacklistToken(String token, Instant expiryTime) {
        blacklistedTokens.put(token, expiryTime);

        // Optional: Clean up expired tokens from the map to prevent memory leaks
        // In a real application, this should be done by a scheduled task
        cleanupExpiredTokens();
    }

    /**
     * Check if a token is blacklisted
     *
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.containsKey(token);
    }

    /**
     * Remove expired tokens from the blacklist This prevents the map from
     * growing indefinitely
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
}
