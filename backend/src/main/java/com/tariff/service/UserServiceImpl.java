package com.tariff.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tariff.entity.User;
import com.tariff.exception.UserNotFoundException;
import com.tariff.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> listUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {   // <-- added
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {  // <-- added
        return userRepository.findByUsername(username);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User createOAuthUser(String email, String username, String firstName, String lastName,
            String profileImageUrl, Boolean emailVerified, String provider, String role) {
        User user = new User(
                username,
                email,
                "", // No password for OAuth users
                role,
                firstName,
                lastName,
                profileImageUrl,
                emailVerified,
                provider
        );
        return userRepository.save(user);
    }

    @Override
    public void createPasswordResetToken(String email, String token) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setResetToken(token);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
            userRepository.save(user);
        }
    }

    @Override
    public boolean isValidPasswordResetToken(String token) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getResetTokenExpiry() != null
                    && user.getResetTokenExpiry().isAfter(LocalDateTime.now());
        }
        return false;
    }

    @Override
    public User findByPasswordResetToken(String token) {
        return userRepository.findByResetToken(token).orElse(null);
    }

    @Override
    public void updatePassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
        }
    }

    @Override
    public void clearPasswordResetToken(String token) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            userRepository.save(user);
        }
    }
}
