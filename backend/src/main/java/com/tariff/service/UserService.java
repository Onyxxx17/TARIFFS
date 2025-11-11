package com.tariff.service;

import java.util.List;
import java.util.Optional;

import com.tariff.entity.User;

public interface UserService {

    List<User> listUser();

    User getUser(Long id);

    Optional<User> getUserByUsername(String username);

    User addUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    // Method for creating OAuth users with additional profile information
    User createOAuthUser(String email, String username, String firstName, String lastName,
            String profileImageUrl, Boolean emailVerified, String provider, String role);

    // Forgot password methods
    void createPasswordResetToken(String email, String token);

    boolean isValidPasswordResetToken(String token);

    User findByPasswordResetToken(String token);

    void updatePassword(String token, String newPassword);

    void clearPasswordResetToken(String token);
}
