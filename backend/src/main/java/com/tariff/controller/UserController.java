package com.tariff.controller;

import com.tariff.entity.User;
import com.tariff.exception.UserAlreadyExistsException;
import com.tariff.service.UserService;

import com.tariff.dto.SignupRequest;
import com.tariff.dto.LoginRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- Signup ---
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
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

        User user = new User(
                request.username(),
                request.email(),
                hashedPassword,
                request.role()
        );

        userService.addUser(user);
        return ResponseEntity.ok("User created");
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

        // later, replace "dummy-jwt-token" with a real JWT
        return ResponseEntity.ok(Map.of(
                "token", "dummy-jwt-token",
                "message", "Login successful"
        ));
    }
}
