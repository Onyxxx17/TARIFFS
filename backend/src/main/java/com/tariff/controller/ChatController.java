package com.tariff.controller;

import com.tariff.DTO.request.ChatRequest;
import com.tariff.DTO.response.ChatResponse;
import com.tariff.ai.dto.TariffQueryRequest;
import com.tariff.ai.dto.TariffQueryResponse;
import com.tariff.ai.service.TariffAIService;
import com.tariff.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
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
    private TariffAIService tariffAIService;
    
    @Autowired
    private GeminiService geminiService;

    @PostMapping("/message")
    @Operation(summary = "Send a message to the AI chatbot")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest request) {
        
        ChatResponse response = new ChatResponse();

        try {
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                response.setMessage("Please provide a message.");
                response.setError(true);
                return ResponseEntity.badRequest().body(response);
            }

            // Use TariffAIService to handle the query (which can extract from database)
            TariffQueryRequest tariffRequest = new TariffQueryRequest(request.getMessage(), "data");
            TariffQueryResponse tariffResponse = tariffAIService.handleQuery(tariffRequest);
            
            // Convert TariffQueryResponse to ChatResponse
            response.setMessage(tariffResponse.response());
            response.setError(false);

        } catch (Exception e) {
            System.err.println("Error in ChatController: " + e.getMessage());
            e.printStackTrace();
            
            // Fallback to basic GeminiService if TariffAIService fails
            try {
                String systemPrompt = "You are a helpful assistant for tariff and trade information. Answer briefly and clearly.";
                String aiResponse = geminiService.generateContent(systemPrompt, request.getMessage());
                response.setMessage(aiResponse);
                response.setError(false);
            } catch (Exception fallbackError) {
                response.setMessage("I'm sorry, I encountered an error: " + e.getMessage());
                response.setError(true);
            }
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    @Operation(summary = "Check chatbot health status")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Chatbot is running");
    }
}
