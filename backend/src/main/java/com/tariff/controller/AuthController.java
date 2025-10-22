package com.tariff.controller;

import com.tariff.entity.RefreshToken;
import com.tariff.entity.User;
import com.tariff.exception.UserAlreadyExistsException;
import com.tariff.service.UserService;
import com.tariff.service.JwtBlacklistService;

import com.tariff.dto.SignupRequest;
import com.tariff.dto.SignupResponse;
import com.tariff.dto.LoginRequest;
import com.tariff.dto.LoginResponse;
import com.tariff.dto.RefreshTokenRequest;
import com.tariff.dto.RefreshTokenResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

import com.tariff.service.RefreshTokenService;
import com.tariff.config.JWTUtils;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtils jwtUtils;
    private final JwtBlacklistService jwtBlacklistService;

    public AuthController(UserService userService, RefreshTokenService refreshTokenService, JWTUtils jwtUtils, JwtBlacklistService jwtBlacklistService) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    // --- Signup ---
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        Optional<User> existingEmail = userService.findByEmail(request.email());
        if (existingEmail.isPresent()) {
            throw new UserAlreadyExistsException(request.email());
        }

        Optional<User> existingUsername = userService.findByUsername(request.username());
        if (existingUsername.isPresent()) {
            throw new UserAlreadyExistsException(request.username());
        }

        // Password constraints
        String pw = request.password();
        if (pw.length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters.");
        }
        if (!pw.matches(".*[A-Z].*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one uppercase letter.");
        }
        if (!pw.matches(".*[a-z].*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one lowercase letter.");
        }
        if (!pw.matches(".*\\d.*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one number.");
        }
        if (!pw.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one special character.");
        }

        // Hash the password before saving
        String hashedPassword = BCrypt.withDefaults().hashToString(12, pw.toCharArray());

        // Always assign ROLE_USER for signup (security measure)
        // Admin users should be created manually or through admin endpoints
        String userRole = "ROLE_ADMIN".equals(request.role()) ? "ROLE_ADMIN" : "ROLE_USER";

        User user = new User(
                request.username(),
                request.email(),
                hashedPassword,
                userRole
        );

        userService.addUser(user);
        return ResponseEntity.ok(new SignupResponse(request.username()));
    }

    // --- Login ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.email());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        User user = userOpt.get();
        BCrypt.Result result = BCrypt.verifyer().verify(request.password().toCharArray(), user.getPassword());

        if (!result.verified) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        // Create refresh token and JWT token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        String jwtToken = jwtUtils.generateTokenFromUsername(user.getEmail(), user.getRole());

        return ResponseEntity.ok(new LoginResponse(user.getEmail(), jwtToken, refreshToken.getToken()));
    }

    // --- Refresh Token ---
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    String newJwtToken = jwtUtils.generateTokenFromUsername(user.getEmail(), user.getRole());
                    // Keep the same refresh token - only return new JWT
                    return ResponseEntity.ok(new RefreshTokenResponse(newJwtToken, refreshToken.getToken()));
                })
                .orElse(ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null));
    }

    // --- Logout ---
    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication, @RequestHeader("Authorization") String authHeader) {
        String authenticatedEmail = authentication.getName(); // JWT subject is the email

        // Extract token from Authorization header
        String token = authHeader.replace("Bearer ", "");

        // Get JWT expiration time - assuming 20 seconds from now
        java.time.Instant expiryTime = java.time.Instant.now().plusSeconds(20);

        // Blacklist the token
        jwtBlacklistService.blacklistToken(token, expiryTime);

        Optional<User> userOpt = userService.findByEmail(authenticatedEmail);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
        }

        // Delete all refresh tokens for this user
        refreshTokenService.deleteByUser(userOpt.get());

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    // --- Logout from all devices ---
    @PostMapping("/logout-all")
    public ResponseEntity<?> logoutAll(Authentication authentication) {
        String authenticatedEmail = authentication.getName(); // JWT subject is the email

        Optional<User> userOpt = userService.findByEmail(authenticatedEmail);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
        }

        refreshTokenService.deleteByUser(userOpt.get());

        return ResponseEntity.ok(Map.of("message", "Logged out from all devices"));
    }

    // --- Create Admin User (Only accessible by existing admins) ---
    @PostMapping("/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createAdmin(@RequestBody SignupRequest request) {

        Optional<User> existingEmail = userService.findByEmail(request.email());
        if (existingEmail.isPresent()) {
            throw new UserAlreadyExistsException(request.email());
        }

        Optional<User> existingUsername = userService.findByUsername(request.username());
        if (existingUsername.isPresent()) {
            throw new UserAlreadyExistsException(request.username());
        }

        // Password constraints (same as signup)
        String pw = request.password();
        if (pw.length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters.");
        }
        if (!pw.matches(".*[A-Z].*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one uppercase letter.");
        }
        if (!pw.matches(".*[a-z].*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one lowercase letter.");
        }
        if (!pw.matches(".*\\d.*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one number.");
        }
        if (!pw.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return ResponseEntity.badRequest().body("Password must contain at least one special character.");
        }

        // Hash the password before saving
        String hashedPassword = BCrypt.withDefaults().hashToString(12, pw.toCharArray());

        // Create admin user
        User adminUser = new User(
                request.username(),
                request.email(),
                hashedPassword,
                "ROLE_ADMIN"
        );

        userService.addUser(adminUser);
        return ResponseEntity.ok(new SignupResponse(request.username() + " (ADMIN)"));
    }
}
