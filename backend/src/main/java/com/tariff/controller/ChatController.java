package com.tariff.controller;

import com.tariff.dto.request.ChatRequest;
import com.tariff.dto.response.ChatResponse;
import com.tariff.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "AI Chatbot endpoints")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private GeminiService geminiService;

    @PostMapping("/message")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Send a message to the AI chatbot")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        
        ChatResponse response = new ChatResponse();

        try {
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                response.setMessage("Please provide a message.");
                response.setError(true);
                return ResponseEntity.badRequest().body(response);
            }

            String aiResponse = geminiService.generateResponse(request.getMessage());
            response.setMessage(aiResponse);
            response.setError(false);

        } catch (Exception e) {
            response.setMessage("I'm sorry, I encountered an error. Please try again.");
            response.setError(true);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Check chatbot health status")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Chatbot is running");
    }
}
