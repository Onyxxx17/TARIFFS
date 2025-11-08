package com.tariff.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.config.GeminiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.List;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final GeminiConfig geminiConfig;
    private final ObjectMapper objectMapper;

    @Autowired
    public GeminiService(WebClient geminiWebClient, GeminiConfig geminiConfig) {
        this.webClient = geminiWebClient;
        this.geminiConfig = geminiConfig;
        this.objectMapper = new ObjectMapper();
    }

    public String generateContent(String systemPrompt, String userPrompt) {
        try {
            // Create the request body for Gemini API
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of(
                        "parts", List.of(
                            Map.of("text", systemPrompt + "\n\nUser Query: " + userPrompt)
                        )
                    )
                ),
                "generationConfig", Map.of(
                    "temperature", 0.7,
                    "maxOutputTokens", 800,
                    "stopSequences", List.of()
                )
            );

            System.out.println("Making request to Gemini API: " + geminiConfig.getApiUrl());
            System.out.println("Request body: " + objectMapper.writeValueAsString(requestBody));

            // Make the API call
            String response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .queryParam("key", geminiConfig.getApiKey())
                            .build())
                    .body(Mono.just(requestBody), Map.class)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), 
                        clientResponse -> clientResponse.bodyToMono(String.class)
                            .map(errorBody -> new RuntimeException("API Error " + clientResponse.statusCode() + ": " + errorBody)))
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Gemini API response: " + response);

            // Parse the response
            JsonNode jsonResponse = objectMapper.readTree(response);
            
            // Check for error in response
            JsonNode error = jsonResponse.get("error");
            if (error != null) {
                String errorMessage = error.get("message") != null ? error.get("message").asText() : "Unknown error";
                throw new RuntimeException("Gemini API error: " + errorMessage);
            }
            
            JsonNode candidates = jsonResponse.get("candidates");
            
            if (candidates != null && candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                
                // Check finish reason
                JsonNode finishReason = firstCandidate.get("finishReason");
                if (finishReason != null) {
                    String reason = finishReason.asText();
                    if ("MAX_TOKENS".equals(reason)) {
                        return "I apologize, but my response was cut off due to length limits. Please ask a more specific or shorter question.";
                    } else if ("SAFETY".equals(reason)) {
                        return "I cannot provide a response to that request due to safety guidelines. Please try rephrasing your question.";
                    } else if ("RECITATION".equals(reason)) {
                        return "I cannot provide that information due to content policies. Please try asking something else.";
                    }
                }
                
                JsonNode content = firstCandidate.get("content");
                if (content != null) {
                    JsonNode parts = content.get("parts");
                    if (parts != null && parts.isArray() && parts.size() > 0) {
                        JsonNode firstPart = parts.get(0);
                        JsonNode text = firstPart.get("text");
                        if (text != null) {
                            return text.asText().trim();
                        }
                    } else {
                        // Handle case where parts array is missing but content exists
                        JsonNode text = content.get("text");
                        if (text != null) {
                            return text.asText().trim();
                        }
                        // If no parts and no direct text, check if there's any text content
                        return "I received your message but couldn't generate a proper response. Please try rephrasing your question.";
                    }
                }
            }
            
            throw new RuntimeException("Invalid response format from Gemini API. Response: " + response);
            
        } catch (Exception e) {
            System.err.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error calling Gemini API: " + e.getMessage(), e);
        }
    }
}