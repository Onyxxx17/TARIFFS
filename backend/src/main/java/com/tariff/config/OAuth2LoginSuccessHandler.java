package com.tariff.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tariff.dto.GoogleOAuth2UserInfo;
import com.tariff.entity.RefreshToken;
import com.tariff.entity.User;
import com.tariff.service.RefreshTokenService;
import com.tariff.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtils jwtUtils;

    @Value("${frontend.url}")
    private String frontendUrl;

    public OAuth2LoginSuccessHandler(UserService userService,
            RefreshTokenService refreshTokenService,
            JWTUtils jwtUtils) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invalid authentication type");
            return;
        }

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();

        String googleId = (String) attributes.getOrDefault("sub", "");
        String email = (String) attributes.getOrDefault("email", "");
        String name = (String) attributes.getOrDefault("name", "");
        String profilePicture = (String) attributes.getOrDefault("picture", null);
        String firstName = (String) attributes.getOrDefault("given_name", null);
        String lastName = (String) attributes.getOrDefault("family_name", null);
        Boolean emailVerified = (Boolean) attributes.getOrDefault("email_verified", Boolean.TRUE);

        GoogleOAuth2UserInfo googleUserInfo = new GoogleOAuth2UserInfo(
                googleId,
                email,
                name,
                profilePicture,
                firstName,
                lastName,
                emailVerified
        );

        User user = userService.findByEmail(googleUserInfo.getEmail())
                .orElseGet(() -> userService.createOAuthUser(
                googleUserInfo.getEmail(),
                googleUserInfo.getEmail(),
                googleUserInfo.getFirstName(),
                googleUserInfo.getLastName(),
                googleUserInfo.getImageUrl(),
                googleUserInfo.getEmailVerified(),
                "GOOGLE",
                "ROLE_USER"
        ));

        String jwtToken = jwtUtils.generateTokenFromUsername(user.getEmail(), user.getRole());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        String redirectUrl = String.format("%s/auth/callback?token=%s&refreshToken=%s&email=%s",
                this.frontendUrl,
                jwtToken,
                refreshToken.getToken(),
                user.getEmail());

        response.sendRedirect(redirectUrl);
    }
}
