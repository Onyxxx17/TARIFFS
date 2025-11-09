package com.tariff.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TariffRuleTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        TariffRule rule = new TariffRule();
        rule.setId(1L);
        rule.setRate(new BigDecimal("10.5"));
        rule.setAdditionalFee(new BigDecimal("5.25"));
        rule.setEffectiveYear(2025);

        assertThat(rule.getId()).isEqualTo(1L);
        assertThat(rule.getRate()).isEqualByComparingTo("10.5");
        assertThat(rule.getAdditionalFee()).isEqualByComparingTo("5.25");
        assertThat(rule.getEffectiveYear()).isEqualTo(2025);
    }

    @Test
    void testAllArgsConstructor() {
        TariffRule rule = new TariffRule(new BigDecimal("12.5"), new BigDecimal("7.5"), 2024);

        assertThat(rule.getRate()).isEqualByComparingTo("12.5");
        assertThat(rule.getAdditionalFee()).isEqualByComparingTo("7.5");
        assertThat(rule.getEffectiveYear()).isEqualTo(2024);
    }

    @Test
    void testPartialConstructor() {
        TariffRule rule = new TariffRule(new BigDecimal("8.5"), 2023);

        assertThat(rule.getRate()).isEqualByComparingTo("8.5");
        assertThat(rule.getEffectiveYear()).isEqualTo(2023);
        assertThat(rule.getAdditionalFee()).isNull();
    }

    @Test
    void testToStringEqualsHashCodeCanEqual() {
        TariffRule rule1 = new TariffRule(new BigDecimal("10"), new BigDecimal("5"), 2025);
        TariffRule rule2 = new TariffRule(new BigDecimal("10"), new BigDecimal("5"), 2025);
        TariffRule rule3 = new TariffRule(new BigDecimal("12"), new BigDecimal("6"), 2026);

        // toString
        assertThat(rule1.toString()).contains("10");

        // equals
        assertThat(rule1).isEqualTo(rule2);
        assertThat(rule1).isNotEqualTo(rule3);

        // hashCode
        assertThat(rule1.hashCode()).isEqualTo(rule2.hashCode());

        // canEqual (used internally by Lombok equals)
        assertThat(rule1.canEqual(rule2)).isTrue();
        assertThat(rule1.canEqual(rule3)).isTrue();
    }
}
