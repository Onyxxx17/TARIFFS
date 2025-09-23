package com.tariff.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tariff.entity.RefreshToken;
import com.tariff.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    // Find refresh token by token string
    Optional<RefreshToken> findByToken(String token);

    // Find all refresh tokens for a user
    Optional<RefreshToken> findByUser(User user);

    // Delete refresh token by token string
    @Modifying
    @Query("DELETE FROM refresh_tokens rt WHERE rt.token = ?1")
    void deleteByToken(String token);

    // Delete all refresh tokens for a user (useful for logout all devices)
    @Modifying
    @Query("DELETE FROM refresh_tokens rt WHERE rt.user = ?1")
    void deleteByUser(User user);

    // Delete expired tokens (cleanup job)
    @Modifying
    @Query("DELETE FROM refresh_tokens rt WHERE rt.expiryDate < ?1")
    void deleteExpiredTokens(LocalDateTime now);

    // Check if token exists and is not expired
    @Query("SELECT COUNT(rt) > 0 FROM refresh_tokens rt WHERE rt.token = ?1 AND rt.expiryDate > ?2")
    boolean existsByTokenAndNotExpired(String token, LocalDateTime now);
}
