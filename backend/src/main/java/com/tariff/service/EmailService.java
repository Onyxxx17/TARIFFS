package com.tariff.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset Request - TariffTracker");

        String resetUrl = baseUrl + "/reset-password?token=" + resetToken;
        String emailBody = "Hello,\n\n"
                + "You have requested to reset your password for your TariffTracker account.\n\n"
                + "Please click the link below to reset your password:\n"
                + resetUrl + "\n\n"
                + "This link will expire in 1 hour for security reasons.\n\n"
                + "If you did not request this password reset, please ignore this email.\n\n"
                + "Best regards,\n"
                + "TariffTracker Team";

        message.setText(emailBody);

        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
