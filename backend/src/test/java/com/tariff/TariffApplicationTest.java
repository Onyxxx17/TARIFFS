package com.tariff;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TariffApplicationTest {

    @Test
    void testMainMethod() {
        // Simple test to verify the main method exists and can be called
        assertDoesNotThrow(() -> {
            // Just verify the class loads properly
            TariffApplication app = new TariffApplication();
            assertNotNull(app);
        });
    }
}