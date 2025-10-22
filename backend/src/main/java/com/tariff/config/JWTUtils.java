package com.tariff.config;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JWTUtils {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.issuer:tariff-application}")
    private String jwtIssuer;

    public JWTUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT token for the authenticated user
     *
     * @param authentication The authenticated user's details
     * @return JWT token string
     */
    public String generateJwtToken(Authentication authentication) {
        Instant now = Instant.now();

        // Convert Spring Security authorities to JWT scope claim
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer) // iss: token issuer
                .issuedAt(now) // iat: issued at time
                .expiresAt(now.plusSeconds(jwtExpiration)) // exp: expiration time
                .subject(authentication.getName()) // sub: user identifier
                .claim("scope", scope) // scope: user authorities
                .build();

        // Create JWT with HMAC SHA-512 signature
        var encoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                claims
        );

        return this.jwtEncoder.encode(encoderParameters).getTokenValue();
    }

    /**
     * Generates a JWT token from username and role (for refresh token flow)
     *
     * @param username The username (email)
     * @param role The user's role (should already have ROLE_ prefix)
     * @return JWT token string
     */
    public String generateTokenFromUsername(String username, String role) {
        Instant now = Instant.now();

        // Role should already have ROLE_ prefix from database
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpiration))
                .subject(username)
                .claim("scope", role)
                .build();

        var encoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                claims
        );

        return this.jwtEncoder.encode(encoderParameters).getTokenValue();
    }
}
