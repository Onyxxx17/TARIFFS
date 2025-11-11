package com.tariff.controller;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tariff.dto.request.ForgotPasswordRequest;
import com.tariff.dto.request.ResetPasswordRequest;
import com.tariff.dto.response.GenericResponse;
import com.tariff.entity.User;
import com.tariff.service.EmailService;
import com.tariff.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Password Reset", description = "Password reset operations")
@CrossOrigin(origins = "http://localhost:3000")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot-password")
    @Operation(summary = "Request password reset", description = "Send password reset email to user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset email sent successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<GenericResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            // Check if user exists
            Optional<User> userOptional = userService.findByEmail(request.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404)
                        .body(new GenericResponse(false, "User with this email does not exist"));
            }

            // Generate reset token
            String resetToken = UUID.randomUUID().toString();

            // Save token to database
            userService.createPasswordResetToken(request.getEmail(), resetToken);

            // Send email
            emailService.sendPasswordResetEmail(request.getEmail(), resetToken);

            return ResponseEntity.ok(
                    new GenericResponse(true, "Password reset instructions have been sent to your email")
            );

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new GenericResponse(false, "An error occurred while processing your request"));
        }
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset user password with token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Password reset successful"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token"),
        @ApiResponse(responseCode = "400", description = "Passwords do not match")
    })
    public ResponseEntity<GenericResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            // Validate token
            if (!userService.isValidPasswordResetToken(request.getToken())) {
                return ResponseEntity.status(400)
                        .body(new GenericResponse(false, "Invalid or expired reset token"));
            }

            // Check if passwords match
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.status(400)
                        .body(new GenericResponse(false, "Passwords do not match"));
            }

            // Update password
            userService.updatePassword(request.getToken(), request.getNewPassword());

            return ResponseEntity.ok(
                    new GenericResponse(true, "Password has been reset successfully")
            );

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new GenericResponse(false, "An error occurred while resetting your password"));
        }
    }

    @GetMapping("/validate-reset-token")
    @Operation(summary = "Validate reset token", description = "Check if reset token is valid")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token is valid"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    public ResponseEntity<GenericResponse> validateResetToken(@RequestParam String token) {
        try {
            boolean isValid = userService.isValidPasswordResetToken(token);

            if (isValid) {
                return ResponseEntity.ok(
                        new GenericResponse(true, "Token is valid")
                );
            } else {
                return ResponseEntity.status(400)
                        .body(new GenericResponse(false, "Invalid or expired token"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new GenericResponse(false, "An error occurred while validating the token"));
        }
    }
}
