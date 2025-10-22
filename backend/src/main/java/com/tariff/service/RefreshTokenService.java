package com.tariff.service;

import java.util.Optional;

import com.tariff.entity.RefreshToken;
import com.tariff.entity.User;

public interface RefreshTokenService {

    /**
     * Create a new refresh token for the given user
     *
     * @param user The user to create the refresh token for
     * @return The created refresh token
     */
    RefreshToken createRefreshToken(User user);

    /**
     * Find refresh token by token string
     *
     * @param token The token string to search for
     * @return Optional containing the refresh token if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Verify if the refresh token is valid (exists and not expired)
     *
     * @param refreshToken The refresh token to verify
     * @return The refresh token if valid
     * @throws RuntimeException if token is expired or invalid
     */
    RefreshToken verifyExpiration(RefreshToken refreshToken);

    /**
     * Delete refresh token by token string
     *
     * @param token The token string to delete
     */
    void deleteByToken(String token);

    /**
     * Delete all refresh tokens for a specific user (logout from all devices)
     *
     * @param user The user whose tokens should be deleted
     */
    void deleteByUser(User user);

    /**
     * Cleanup expired tokens from the database
     *
     * @return Number of tokens deleted
     */
    int deleteExpiredTokens();

    /**
     * Check if a token exists and is not expired
     *
     * @param token The token string to check
     * @return true if token is valid, false otherwise
     */
    boolean isTokenValid(String token);

    /**
     * Generate a new refresh token for an existing user This will delete the
     * old token and create a new one
     *
     * @param user The user to generate a new token for
     * @return The new refresh token
     */
    RefreshToken refreshToken(User user);
}
