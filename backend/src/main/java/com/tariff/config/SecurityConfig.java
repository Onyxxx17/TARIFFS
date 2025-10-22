package com.tariff.config;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.tariff.service.JwtBlacklistService;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    // Secret key for signing JWT tokens, configurable via application.properties
    @Value("${jwt.secret:myVerySecretKeyForTariffApplication123456789}")
    private String jwtSecret;

    /**
     * JWT-based authentication and authorization configuration User role: can
     * add review. Admin role: can add/delete/update books/reviews, and add/list
     * users
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtBlacklistService jwtBlacklistService) throws Exception {
        // Create JWT filters
        JwtBlacklistFilter blacklistFilter = new JwtBlacklistFilter(jwtBlacklistService);
        JwtExpirationFilter expirationFilter = new JwtExpirationFilter(jwtBlacklistService);
        
        http
                // Add expiration filter first, then blacklist filter
                .addFilterBefore(expirationFilter, org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter.class)
                .addFilterAfter(blacklistFilter, JwtExpirationFilter.class)
                .authorizeHttpRequests((authz) -> authz
                .requestMatchers("/error").permitAll() // the default error page
                .requestMatchers("/api/auth/login").permitAll() // login endpoint
                .requestMatchers("/api/auth/signup").permitAll() // signup endpoint  
                .requestMatchers("/api/auth/refresh").permitAll() // refresh token endpoint
                .requestMatchers("/api/auth/create-admin").hasRole("ADMIN") // only admins can create admins
                .requestMatchers("/api/auth/logout").authenticated() // logout requires authentication
                .requestMatchers("/api/auth/logout-all").authenticated() // logout-all requires authentication
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll() // Swagger UI
                .requestMatchers("/h2-console/**").permitAll() // H2 Console

                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/countries/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/countries/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/countries/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/countries/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/industries/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/industries/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/industries/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/industries/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/tariff/*").authenticated()
                .anyRequest().authenticated() // all other requests require authentication
                )
                // Configure for stateless JWT authentication
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure OAuth2 Resource Server for JWT
                .oauth2ResourceServer((oauth2) -> oauth2
                .jwt(jwt -> jwt
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
                )
                .cors(Customizer.withDefaults()) // CORS configuration
                .csrf(csrf -> csrf.disable()) // CSRF protection disabled for REST APIs
                .formLogin(form -> form.disable()) // disable form login
                .httpBasic(basic -> basic.disable()) // disable basic auth - good practice for JWT-only APIs
                .headers(header -> header.disable()); // disable security headers for APIs
        return http.build();
    }

    /**
     * JWT Authentication Converter to extract authorities from JWT token -
     * Converts JWT "scope" claim to Spring Security authorities
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix(""); // Remove default SCOPE_ prefix
        authoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return authenticationConverter;
    }

    /**
     * JWT Encoder for creating tokens during login - Used by JwtUtils to
     * generate signed JWT tokens - Applies HMAC SHA-512 signature using the
     * secret key - Required for token generation, not token validation
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecret.getBytes()));
    }

    /**
     * JWT Decoder for validating tokens
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = jwtSecret.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(bytes, 0, bytes.length, "HmacSHA512");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    /**
     * Authentication Manager for processing authentication requests
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Password encoder for hashing passwords
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
