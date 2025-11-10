package com.tariff.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        // no-args constructor
        User user = new User();
        user.setId(1L);
        user.setUsername("user1");
        user.setEmail("user1@mail.com");
        user.setPassword("pass");
        user.setRole("ADMIN");
        user.setImportRecord(List.of());
        user.setRefreshTokens(List.of());

        // getters
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("user1");
        assertThat(user.getEmail()).isEqualTo("user1@mail.com");
        assertThat(user.getPassword()).isEqualTo("pass");
        assertThat(user.getRole()).isEqualTo("ADMIN");
        assertThat(user.getImportRecord()).isNotNull();
        assertThat(user.getRefreshTokens()).isNotNull();
    }

    @Test
    void testAllArgsConstructor() {
        List<ImportRecord> imports = List.of();
        List<RefreshToken> tokens = List.of();

        User user = new User(1L, "user1", "user1@mail.com", "pass", "ADMIN", imports, tokens);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("user1");
        assertThat(user.getEmail()).isEqualTo("user1@mail.com");
        assertThat(user.getPassword()).isEqualTo("pass");
        assertThat(user.getRole()).isEqualTo("ADMIN");
        assertThat(user.getImportRecord()).isEqualTo(imports);
        assertThat(user.getRefreshTokens()).isEqualTo(tokens);
    }

    @Test
    void testToStringEqualsHashCodeCanEqual() {
        User user1 = new User(1L, "user1", "user1@mail.com", "pass", "ADMIN", List.of(), List.of());
        User user2 = new User(1L, "user1", "user1@mail.com", "pass", "ADMIN", List.of(), List.of());
        User user3 = new User(2L, "user2", "user2@mail.com", "pass2", "USER", List.of(), List.of());

        // toString
        String toString = user1.toString();
        assertThat(toString).contains("user1");

        // equals
        assertThat(user1).isEqualTo(user2);
        assertThat(user1).isNotEqualTo(user3);

        // hashCode
        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());

        // canEqual (Lombok method used internally by equals)
        assertThat(user1.canEqual(user2)).isTrue();
        assertThat(user1.canEqual(user3)).isTrue(); // canEqual only checks type
    }
}
