package com.tariff.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.tariff.config.GeminiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    @Autowired
    private GeminiConfig geminiConfig;

    public String generateResponse(String userMessage) {
        try {
            // Initialize Gemini client
            Client client = new Client.Builder()
                    .apiKey(geminiConfig.getKey())
                    .build();

            // System instruction: domain-specific + fallback logic
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .systemInstruction(Content.fromParts(Part.fromText("""
                        You are TariffAI — a specialized assistant focused on international trade tariffs,
                        customs duties, and import/export regulations.

                        Your core responsibilities:
                        - Clearly explain what tariffs are and how they work.
                        - Help users understand tariff categories, duty rates, and import/export procedures.
                        - Provide short and factual explanations for trade concepts (no unnecessary detail).
                        - If users ask for specific tariff rates or calculations that you cannot confirm,
                          politely direct them to the "Tariff Calculator" tool available on the homepage.

                        Example fallback message:
                        "I recommend checking the Tariff Calculator on the homepage for the most accurate cost estimates."

                        Always use a professional but friendly tone.
                    """)))
                    .temperature((float) geminiConfig.getTemperature())
                    .build();

            // Generate response from Gemini
            GenerateContentResponse response = client.models
                    .generateContent(geminiConfig.getModel(), userMessage, config);

            String text = response.text();

            // If Gemini doesn’t produce anything useful
            if (text == null || text.trim().isEmpty()) {
                return "I'm not certain about that. Please check the Tariff Calculator tool on the homepage for more details.";
            }

            return text;

        } catch (Exception e) {
            return "I'm sorry, I encountered an error while processing your request. Please try again later or use the Tariff Calculator on the homepage. Error: " + e.getMessage();
        }
    }
}
