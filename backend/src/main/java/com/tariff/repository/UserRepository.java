package com.tariff.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tariff.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (for login)
    Optional<User> findByUsername(String username);

    // Find user by email (for login/registration)
    Optional<User> findByEmail(String email);

    // Find users by role
    List<User> findByRole(String role);

    // Check if username exists (for registration validation)
    boolean existsByUsername(String username);

    // Check if email exists (for registration validation)
    boolean existsByEmail(String email);

    // Find by username or email (flexible login)
    Optional<User> findByUsernameOrEmail(String username, String email);

    // Find user by password reset token (for forgot password)
    Optional<User> findByResetToken(String resetToken);
}
