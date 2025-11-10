package com.tariff.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TariffRuleTest {

    @Test
    void testNoArgsConstructorAndSettersGetters() {
        TariffRule rule = new TariffRule();
        rule.setId(1L);
        rule.setRate(new BigDecimal("10.5"));
        rule.setAdditionalFees(Arrays.asList(new BigDecimal("5.25")));
        rule.setEffectiveYear(2025);

        assertThat(rule.getId()).isEqualTo(1L);
        assertThat(rule.getRate()).isEqualByComparingTo("10.5");
        assertThat(rule.getAdditionalFees()).hasSize(1);
        assertThat(rule.getAdditionalFees().get(0)).isEqualByComparingTo("5.25");
        assertThat(rule.getEffectiveYear()).isEqualTo(2025);
    }

    @Test
    void testAllArgsConstructor() {
        List<BigDecimal> additionalFees = Arrays.asList(new BigDecimal("7.5"));
        TariffRule rule = new TariffRule(new BigDecimal("12.5"), additionalFees, 2024);

        assertThat(rule.getRate()).isEqualByComparingTo("12.5");
        assertThat(rule.getAdditionalFees()).hasSize(1);
        assertThat(rule.getAdditionalFees().get(0)).isEqualByComparingTo("7.5");
        assertThat(rule.getEffectiveYear()).isEqualTo(2024);
    }

    @Test
    void testPartialConstructor() {
        TariffRule rule = new TariffRule(new BigDecimal("8.5"), 2023);

        assertThat(rule.getRate()).isEqualByComparingTo("8.5");
        assertThat(rule.getEffectiveYear()).isEqualTo(2023);
        assertThat(rule.getAdditionalFees()).isEmpty();
    }

    @Test
    void testToStringEqualsHashCodeCanEqual() {
        List<BigDecimal> fees1 = Arrays.asList(new BigDecimal("5"));
        List<BigDecimal> fees2 = Arrays.asList(new BigDecimal("5"));
        List<BigDecimal> fees3 = Arrays.asList(new BigDecimal("6"));

        TariffRule rule1 = new TariffRule(new BigDecimal("10"), fees1, 2025);
        TariffRule rule2 = new TariffRule(new BigDecimal("10"), fees2, 2025);
        TariffRule rule3 = new TariffRule(new BigDecimal("12"), fees3, 2026);

        // toString
        assertThat(rule1.toString()).contains("10");

        // equals
        assertThat(rule1).isEqualTo(rule2);
        assertThat(rule1).isNotEqualTo(rule3);

        // hashCode
        assertThat(rule1.hashCode()).isEqualTo(rule2.hashCode());
    }
}
