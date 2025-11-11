package com.tariff.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JwtBlacklistServiceTest {

    @InjectMocks
    private JwtBlacklistService jwtBlacklistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Reset the blacklist before each test
        ReflectionTestUtils.setField(jwtBlacklistService, "blacklistedTokens", new ConcurrentHashMap<>());
    }

    @Test
    public void testBlacklistToken() {
        String token = "test-token";
        Instant expiry = Instant.now().plusSeconds(3600);

        jwtBlacklistService.blacklistToken(token, expiry);

        assertTrue(jwtBlacklistService.isBlacklisted(token));
    }

    @Test
    public void testIsBlacklisted_False() {
        assertFalse(jwtBlacklistService.isBlacklisted("not-blacklisted-token"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCleanupExpiredTokens() throws Exception {
        Map<String, Instant> blacklistedTokens = (Map<String, Instant>) ReflectionTestUtils.getField(jwtBlacklistService, "blacklistedTokens");
        assertNotNull(blacklistedTokens);

        String expiredToken = "expired-token";
        String validToken = "valid-token";

        blacklistedTokens.put(expiredToken, Instant.now().minusSeconds(60));
        blacklistedTokens.put(validToken, Instant.now().plusSeconds(60));

        jwtBlacklistService.cleanupExpiredTokens();

        assertFalse(blacklistedTokens.containsKey(expiredToken));
        assertTrue(blacklistedTokens.containsKey(validToken));
    }
}
