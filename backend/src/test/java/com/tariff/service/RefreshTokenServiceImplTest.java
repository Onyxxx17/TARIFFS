package com.tariff.service;

import com.tariff.entity.RefreshToken;
import com.tariff.entity.User;
import com.tariff.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RefreshTokenServiceImplTest {

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 3600000L); // 1 hour
    }

    @Test
    public void testCreateRefreshToken() {
        User user = new User();
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        RefreshToken token = refreshTokenService.createRefreshToken(user);

        assertNotNull(token);
        assertNotNull(token.getToken());
        assertFalse(token.isExpired());
        assertEquals(user, token.getUser());
    }

    @Test
    public void testFindByToken() {
        RefreshToken token = new RefreshToken();
        when(refreshTokenRepository.findByToken("test-token")).thenReturn(Optional.of(token));

        Optional<RefreshToken> foundToken = refreshTokenService.findByToken("test-token");

        assertTrue(foundToken.isPresent());
        assertEquals(token, foundToken.get());
    }

    @Test
    public void testVerifyExpiration_NotExpired() {
        RefreshToken token = new RefreshToken("test", LocalDateTime.now().plusHours(1), new User());
        RefreshToken result = refreshTokenService.verifyExpiration(token);
        assertEquals(token, result);
    }

    @Test
    public void testVerifyExpiration_Expired() {
        RefreshToken token = new RefreshToken("test", LocalDateTime.now().minusHours(1), new User());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            refreshTokenService.verifyExpiration(token);
        });

        assertEquals("Refresh token was expired. Please make a new signin request", exception.getMessage());
        verify(refreshTokenRepository, times(1)).delete(token);
    }

    @Test
    public void testDeleteByToken() {
        refreshTokenService.deleteByToken("test-token");
        verify(refreshTokenRepository, times(1)).deleteByToken("test-token");
    }

    @Test
    public void testDeleteByUser() {
        User user = new User();
        refreshTokenService.deleteByUser(user);
        verify(refreshTokenRepository, times(1)).deleteByUser(user);
    }

    @Test
    public void testDeleteExpiredTokens() {
        when(refreshTokenRepository.count()).thenReturn(10L).thenReturn(5L);
        doNothing().when(refreshTokenRepository).deleteExpiredTokens(any(LocalDateTime.class));

        int deletedCount = refreshTokenService.deleteExpiredTokens();

        assertEquals(5, deletedCount);
    }

    @Test
    public void testIsTokenValid_True() {
        when(refreshTokenRepository.existsByTokenAndNotExpired(eq("valid-token"), any(LocalDateTime.class))).thenReturn(true);
        assertTrue(refreshTokenService.isTokenValid("valid-token"));
    }

    @Test
    public void testIsTokenValid_False() {
        when(refreshTokenRepository.existsByTokenAndNotExpired(eq("invalid-token"), any(LocalDateTime.class))).thenReturn(false);
        assertFalse(refreshTokenService.isTokenValid("invalid-token"));
    }

    @Test
    public void testRefreshToken() {
        User user = new User();
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        RefreshToken token = refreshTokenService.refreshToken(user);

        assertNotNull(token);
        assertNotNull(token.getToken());
    }
}
