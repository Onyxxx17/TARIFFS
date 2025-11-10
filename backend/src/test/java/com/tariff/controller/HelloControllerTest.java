package com.tariff.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HelloControllerTest {

    private HelloController helloController;

    @BeforeEach
    void setUp() {
        helloController = new HelloController();
    }

    @Test
    void testHello() {
        String result = helloController.hello();
        assertEquals("Hello World from Tariffs API!", result);
    }

    @Test
    void testHelloWithName() {
        String name = "Alice";
        String result = helloController.helloWithName(name);
        assertEquals("Hello Alice from Tariffs API!", result);
    }

    @Test
    void testStatus() {
        String result = helloController.status();
        assertEquals("Tariffs Backend is running successfully!", result);
    }
}
