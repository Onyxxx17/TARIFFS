package com.tariff.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tariff.entity.RefreshToken;
import com.tariff.entity.User;
import com.tariff.repository.RefreshTokenRepository;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        // Don't delete existing tokens - allow multiple tokens per user for multi-device support
        // Only expired tokens should be cleaned up separately

        // Create new refresh token using constructor
        String tokenString = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000);

        RefreshToken refreshToken = new RefreshToken(tokenString, expiryDate, user);

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public int deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        // Count expired tokens before deletion
        long countBefore = refreshTokenRepository.count();
        refreshTokenRepository.deleteExpiredTokens(now);
        long countAfter = refreshTokenRepository.count();
        return (int) (countBefore - countAfter);
    }

    @Override
    public boolean isTokenValid(String token) {
        return refreshTokenRepository.existsByTokenAndNotExpired(token, LocalDateTime.now());
    }

    @Override
    public RefreshToken refreshToken(User user) {
        return createRefreshToken(user);
    }
}
