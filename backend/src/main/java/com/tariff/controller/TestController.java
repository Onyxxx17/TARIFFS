package com.tariff.controller;

import com.tariff.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private GeminiService geminiService;

    @GetMapping("/gemini")
    public ResponseEntity<String> testGemini(@RequestParam(defaultValue = "Hello, how are you?") String message) {
        try {
            String systemPrompt = "You are a helpful AI assistant. Respond briefly and clearly.";
            String response = geminiService.generateContent(systemPrompt, message);
            return ResponseEntity.ok("SUCCESS: " + response);
        } catch (Exception e) {
            return ResponseEntity.ok("ERROR: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Test controller is working");
    }
}