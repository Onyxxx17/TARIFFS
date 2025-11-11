package com.tariff.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.tariff.entity.User;
import com.tariff.exception.UserNotFoundException;
import com.tariff.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("john");
        user1.setPassword("pass123");
        user1.setRole("ADMIN");
    }

    @Test
    void testListUser() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1));

        List<User> result = userService.listUser();
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        User result = userService.getUser(1L);
        assertEquals("john", result.getUsername());
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(1L));
    }

    @Test
    void testGetUserByUsernameFound() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user1));

        Optional<User> result = userService.getUserByUsername("john");
        assertTrue(result.isPresent());
        assertEquals("john", result.get().getUsername());
    }

    @Test
    void testGetUserByUsernameNotFound() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByUsername("alice");
        assertFalse(result.isPresent());
    }

    @Test
    void testAddUser() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.addUser(user1);
        assertEquals("john", result.getUsername());
        verify(userRepository).save(user1);
    }

    @Test
    void testUpdateUserFound() {
        User updatedUser = new User();
        updatedUser.setUsername("johnny");
        updatedUser.setPassword("newpass");
        updatedUser.setRole("USER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUser(1L, updatedUser);
        assertEquals("johnny", result.getUsername());
        assertEquals("newpass", result.getPassword());
        assertEquals("USER", result.getRole());
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, user1));
    }

    @Test
    void testDeleteUserFound() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void testFindByEmail_Found() {
        String email = "test@example.com";
        user1.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindByEmail_NotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertFalse(result.isPresent());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindByUsername_Found() {
        String username = "john";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user1));

        Optional<User> result = userService.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testFindByUsername_NotFound() {
        String username = "not-john";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername(username);

        assertFalse(result.isPresent());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testCreateOAuthUser() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User oauthUser = userService.createOAuthUser("oauth@test.com", "oauthuser", "OAuth", "User", "image.url", true, "google", "USER");

        assertNotNull(oauthUser);
        assertEquals("oauth@test.com", oauthUser.getEmail());
        assertEquals("oauthuser", oauthUser.getUsername());
        assertEquals("google", oauthUser.getProvider());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreatePasswordResetToken() {
        String email = "test@example.com";
        String token = "reset-token-123";
        user1.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user1));

        userService.createPasswordResetToken(email, token);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(token, savedUser.getResetToken());
        assertNotNull(savedUser.getResetTokenExpiry());
        assertTrue(savedUser.getResetTokenExpiry().isAfter(LocalDateTime.now()));
    }

    @Test
    void testIsValidPasswordResetToken_Valid() {
        String token = "valid-token";
        user1.setResetToken(token);
        user1.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user1));

        boolean isValid = userService.isValidPasswordResetToken(token);

        assertTrue(isValid);
    }

    @Test
    void testIsValidPasswordResetToken_Expired() {
        String token = "expired-token";
        user1.setResetToken(token);
        user1.setResetTokenExpiry(LocalDateTime.now().minusHours(1));
        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user1));

        boolean isValid = userService.isValidPasswordResetToken(token);

        assertFalse(isValid);
    }

    @Test
    void testIsValidPasswordResetToken_NotFound() {
        String token = "not-found-token";
        when(userRepository.findByResetToken(token)).thenReturn(Optional.empty());

        boolean isValid = userService.isValidPasswordResetToken(token);

        assertFalse(isValid);
    }

    @Test
    void testFindByPasswordResetToken_Found() {
        String token = "found-token";
        user1.setResetToken(token);
        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user1));

        User result = userService.findByPasswordResetToken(token);

        assertNotNull(result);
        assertEquals(token, result.getResetToken());
    }

    @Test
    void testFindByPasswordResetToken_NotFound() {
        String token = "not-found-token";
        when(userRepository.findByResetToken(token)).thenReturn(Optional.empty());

        User result = userService.findByPasswordResetToken(token);

        assertNull(result);
    }

    @Test
    void testUpdatePassword() {
        String token = "update-pass-token";
        String newPassword = "newPassword123";
        String encodedPassword = "encodedPassword123";
        user1.setResetToken(token);
        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user1));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        userService.updatePassword(token, newPassword);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(encodedPassword, savedUser.getPassword());
        assertNull(savedUser.getResetToken());
        assertNull(savedUser.getResetTokenExpiry());
    }

    @Test
    void testClearPasswordResetToken() {
        String token = "clear-token";
        user1.setResetToken(token);
        user1.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        when(userRepository.findByResetToken(token)).thenReturn(Optional.of(user1));

        userService.clearPasswordResetToken(token);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertNull(savedUser.getResetToken());
        assertNull(savedUser.getResetTokenExpiry());
    }
}