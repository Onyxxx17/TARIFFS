package com.tariff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.dto.request.ForgotPasswordRequest;
import com.tariff.dto.request.ResetPasswordRequest;
import com.tariff.dto.response.GenericResponse;
import com.tariff.entity.User;
import com.tariff.service.EmailService;
import com.tariff.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.tariff.config.SecurityConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = ForgotPasswordController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
    })
@AutoConfigureMockMvc(addFilters = false)
class ForgotPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
    }

    @Test
    void testForgotPassword_Success() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest("test@example.com");
        when(userService.findByEmail(anyString())).thenReturn(Optional.of(user));
        doNothing().when(userService).createPasswordResetToken(anyString(), anyString());
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password reset instructions have been sent to your email"));
    }

    @Test
    void testForgotPassword_UserNotFound() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest("nonexistent@example.com");
        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("User with this email does not exist"));
    }

    @Test
    void testForgotPassword_InternalServerError() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest("test@example.com");
        when(userService.findByEmail(anyString())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An error occurred while processing your request"));
    }

    @Test
    void testResetPassword_Success() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "newPassword123", "newPassword123");
        when(userService.isValidPasswordResetToken(anyString())).thenReturn(true);
        doNothing().when(userService).updatePassword(anyString(), anyString());

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password has been reset successfully"));
    }

    @Test
    void testResetPassword_InvalidToken() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("invalid-token", "newPassword123", "newPassword123");
        when(userService.isValidPasswordResetToken(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid or expired reset token"));
    }

    @Test
    void testResetPassword_PasswordsDoNotMatch() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "newPassword123", "differentPassword");
        when(userService.isValidPasswordResetToken(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Passwords do not match"));
    }

    @Test
    void testResetPassword_InternalServerError() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "newPassword123", "newPassword123");
        when(userService.isValidPasswordResetToken(anyString())).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An error occurred while resetting your password"));
    }

    @Test
    void testValidateResetToken_Valid() throws Exception {
        when(userService.isValidPasswordResetToken("valid-token")).thenReturn(true);

        mockMvc.perform(get("/api/auth/validate-reset-token").param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token is valid"));
    }

    @Test
    void testValidateResetToken_Invalid() throws Exception {
        when(userService.isValidPasswordResetToken("invalid-token")).thenReturn(false);

        mockMvc.perform(get("/api/auth/validate-reset-token").param("token", "invalid-token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void testValidateResetToken_InternalServerError() throws Exception {
        when(userService.isValidPasswordResetToken("any-token")).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/auth/validate-reset-token").param("token", "any-token"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An error occurred while validating the token"));
    }
}
