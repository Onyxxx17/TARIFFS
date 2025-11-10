package com.tariff.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.tariff.config.GeminiConfig;
import com.tariff.mcp.tools.McpTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GeminiService {

    @Autowired
    private GeminiConfig geminiConfig;

    @Autowired
    private List<McpTool> mcpTools;

    @Autowired
    private ObjectMapper objectMapper;

    public String generateResponse(String userMessage) {
        try {
            // Check if user is asking for specific database data
            String toolResponse = detectAndExecuteTool(userMessage);
            if (toolResponse != null) {
                return toolResponse;
            }

            // Initialize Gemini client for general questions
            Client client = new Client.Builder()
                    .apiKey(geminiConfig.getKey())
                    .build();

            // System instruction: domain-specific
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .systemInstruction(Content.fromParts(Part.fromText("""
                        You are TariffAI — a specialized assistant focused on international trade tariffs,
                        customs duties, and import/export regulations.

                        Your core responsibilities:
                        - Clearly explain what tariffs are and how they work
                        - Help users understand tariff categories, duty rates, and import/export procedures
                        - Provide short and factual explanations for trade concepts
                        - Use a professional but friendly tone
                        
                        When users ask about specific products or tariff rates in the database, 
                        let them know they can ask specific questions like:
                        - "Search for [product name]" to find products
                        - "What's the tariff rate from [country] to [country]?" for specific rates
                    """)))
                    .temperature((float) geminiConfig.getTemperature())
                    .build();

            // Generate response from Gemini
            GenerateContentResponse response = client.models
                    .generateContent(geminiConfig.getModel(), userMessage, config);

            String text = response.text();

            // If Gemini doesn't produce anything useful
            if (text == null || text.trim().isEmpty()) {
                return "I'm not certain about that. You can also use the Tariff Calculator on the homepage for specific calculations.";
            }

            // Remove markdown formatting but keep structure
            return cleanMarkdownFormatting(text);

        } catch (Exception e) {
            return "I'm sorry, I encountered an error while processing your request. Please try again later. Error: " + e.getMessage();
        }
    }

    /**
     * Detect if user message requires database tool and execute it
     */
    private String detectAndExecuteTool(String userMessage) {
        try {
            String lowerMessage = userMessage.toLowerCase();

            // Pattern 1: Search for products
            if (lowerMessage.contains("search") || lowerMessage.contains("find")
                    || lowerMessage.contains("show me") || lowerMessage.contains("list")
                    || lowerMessage.contains("do you have") || lowerMessage.contains("products")) {

                // Extract search query
                String query = extractSearchQuery(userMessage);
                if (query != null && !query.trim().isEmpty()) {
                    for (McpTool tool : mcpTools) {
                        if (tool.getDefinition().getName().equals("search_tariffs")) {
                            // Use empty string for "list all" instead of "*"
                            String searchQuery = "*".equals(query) ? "" : query;
                            Object result = tool.execute(Map.of("query", searchQuery, "limit", 20));
                            return formatToolResponse(result, "product search");
                        }
                    }
                }
            }

            // Pattern 2: Get tariff rates
            if (lowerMessage.contains("tariff rate") || lowerMessage.contains("duty")
                    || lowerMessage.contains("additional fee") || lowerMessage.contains("from") && lowerMessage.contains("to")) {

                Map<String, String> countries = extractCountries(userMessage);
                if (countries != null && countries.containsKey("from") && countries.containsKey("to")) {
                    // Try to extract product name from the query
                    String productQuery = extractProductFromTariffQuery(userMessage);
                    Long productId = null;
                    boolean productWasRequested = (productQuery != null && !productQuery.trim().isEmpty());
                    boolean productWasFound = false;

                    // If a product was mentioned, search for it first
                    if (productWasRequested) {
                        for (McpTool searchTool : mcpTools) {
                            if (searchTool.getDefinition().getName().equals("search_tariffs")) {
                                try {
                                    Object searchResult = searchTool.execute(Map.of("query", productQuery, "limit", 1));
                                    @SuppressWarnings("unchecked")
                                    Map<String, Object> data = (Map<String, Object>) searchResult;
                                    @SuppressWarnings("unchecked")
                                    List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("results");

                                    if (results != null && !results.isEmpty()) {
                                        productId = ((Number) results.get(0).get("id")).longValue();
                                        productWasFound = true;
                                    }
                                } catch (Exception e) {
                                    // If product search fails, continue without product ID
                                }
                                break;
                            }
                        }
                    }

                    for (McpTool tool : mcpTools) {
                        if (tool.getDefinition().getName().equals("get_tariff_rate")) {
                            Map<String, Object> args = new HashMap<>();
                            args.put("fromCountry", countries.get("from"));
                            args.put("toCountry", countries.get("to"));
                            if (productId != null) {
                                args.put("productId", productId);
                            }
                            // Pass metadata to help with formatting
                            args.put("_productRequested", productWasRequested);
                            args.put("_productFound", productWasFound);
                            args.put("_productQuery", productQuery != null ? productQuery : "");

                            Object result = tool.execute(args);
                            return formatToolResponse(result, "tariff rate");
                        }
                    }
                }
            }

        } catch (Exception e) {
            // If tool execution fails, return null to fall back to general AI response
            return null;
        }

        return null;
    }

    /**
     * Extract search query from user message
     */
    private String extractSearchQuery(String message) {
        String lower = message.toLowerCase();

        // Check if user wants to list ALL products (no specific product mentioned)
        if (lower.matches(".*(what|list|show|all).*(products?|items?).*(do you have|available|exist).*")
                || lower.matches(".*(do you have|show me|list).*(all|the)?.*(products?|items?).*")
                && !lower.matches(".*(any|some)\\s+\\w+\\s+(products?|items?).*")) {
            return "*"; // Special marker for "list all"
        }

        // Pattern: "are there any X products" or "do you have X products" or "is there X"
        Pattern specificPattern = Pattern.compile(
                "(?:are there|is there|do you have|any)\\s+(?:any\\s+)?([\\w\\s]+?)\\s+(?:products?|items?)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = specificPattern.matcher(lower);
        if (matcher.find()) {
            String extracted = matcher.group(1).trim();
            // Clean up common words
            extracted = extracted.replaceAll("^(a|an|the|some)\\s+", "");
            return extracted.isEmpty() ? "*" : extracted;
        }

        // Remove common prefixes
        String query = lower
                .replaceAll("^(search for|find|show me|list|do you have|what|any)\\s+", "")
                .replaceAll("\\s+(products?|items?)\\s*$", "")
                .trim();

        // If nothing left after cleaning, return marker for "list all"
        return query.isEmpty() ? "*" : query;
    }

    /**
     * Extract country names/codes from message Normalizes to database country
     * names: "United States of America" and "People's Republic of China"
     */
    private Map<String, String> extractCountries(String message) {
        Map<String, String> countries = new HashMap<>();

        // Pattern: "from X to Y" - more precise matching
        // Stop at common end-of-sentence markers or extra words
        Pattern pattern = Pattern.compile(
                "from\\s+([\\w\\s]+?)\\s+to\\s+([\\w\\s']+?)(?:\\s+(?:if|in|for|with|about|\\?|\\.|,)|\\?|\\.|$)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String from = matcher.group(1).trim();
            String to = matcher.group(2).trim();

            // Clean up extra words that might have been captured
            from = from.replaceAll("\\s+(if|in|for|with)$", "").trim();
            to = to.replaceAll("\\s+(if|in|for|with)$", "").trim();

            // Normalize country names to match database
            from = normalizeCountryName(from);
            to = normalizeCountryName(to);

            countries.put("from", from);
            countries.put("to", to);
        }

        return countries.isEmpty() ? null : countries;
    }

    /**
     * Normalize country names to match database entries Database only has:
     * "United States of America" and "People's Republic of China"
     */
    private String normalizeCountryName(String country) {
        String lower = country.toLowerCase().trim();

        // United States variations
        if (lower.matches(".*\\b(united states|usa|us|america)\\b.*")) {
            return "United States of America";
        }

        // China variations
        if (lower.matches(".*\\b(china|prc|people's republic)\\b.*")) {
            return "People's Republic of China";
        }

        // North Korea variations  
        if (lower.matches(".*\\b(north korea|dprk|korea)\\b.*")) {
            return "North Korea";
        }

        // Return original if no match (will let backend handle the error)
        return country;
    }

    /**
     * Extract product name from tariff rate queries Examples: "tariff rate for
     * Goats from X to Y" -> "Goats" "What's the tariff for Steel from X to Y"
     * -> "Steel"
     *
     * Note: Use more precise patterns to avoid catching country names
     */
    private String extractProductFromTariffQuery(String message) {
        // Pattern 1: "tariff rate for X from" or "tariff for X from"
        // Use non-greedy match and stop at "from" to avoid catching country names
        Pattern pattern1 = Pattern.compile(
                "(?:tariff rate|tariff|duty)\\s+for\\s+([\\w;,\\s-]+?)\\s+from",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher1 = pattern1.matcher(message);
        if (matcher1.find()) {
            String product = matcher1.group(1).trim();
            // Clean up common words and trailing punctuation
            product = product.replaceAll("^(a|an|the|some)\\s+", "");
            product = product.replaceAll("[;,]+$", ""); // Remove trailing semicolons/commas

            // Don't return country names as products
            if (product.equalsIgnoreCase("United States")
                    || product.equalsIgnoreCase("United States of America")
                    || product.equalsIgnoreCase("America")
                    || product.equalsIgnoreCase("China")
                    || product.equalsIgnoreCase("People's Republic of China")
                    || product.equalsIgnoreCase("North Korea")
                    || product.equalsIgnoreCase("Korea")) {
                return null;
            }

            return product;
        }

        // Pattern 2: "What's the tariff rate from X to Y for Z" (product at end)
        // This pattern should NOT trigger for queries without explicit "for" before product
        Pattern pattern2 = Pattern.compile(
                "to\\s+[\\w\\s]+?\\s+(?:for|of)\\s+([\\w;,\\s-]+?)(?:\\?|\\.|$)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher2 = pattern2.matcher(message);
        if (matcher2.find()) {
            String product = matcher2.group(1).trim();
            product = product.replaceAll("^(a|an|the|some)\\s+", "");
            product = product.replaceAll("[;,]+$", "");

            // Don't return country names
            if (product.equalsIgnoreCase("United States")
                    || product.equalsIgnoreCase("United States of America")
                    || product.equalsIgnoreCase("America")
                    || product.equalsIgnoreCase("China")
                    || product.equalsIgnoreCase("People's Republic of China")
                    || product.equalsIgnoreCase("North Korea")
                    || product.equalsIgnoreCase("Korea")) {
                return null;
            }

            return product;
        }

        return null;
    }

    /**
     * Format tool response into natural language
     */
    private String formatToolResponse(Object result, String queryType) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) result;

            if ("product search".equals(queryType)) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("results");
                int count = ((Number) data.get("count")).intValue();
                String query = data.get("query") != null ? data.get("query").toString() : "";

                if (count == 0) {
                    return "I couldn't find any products matching your search. Please try different keywords.";
                }

                StringBuilder response = new StringBuilder();

                // Better message for "list all" vs "search specific"
                if (query == null || query.trim().isEmpty()) {
                    response.append("Here are ").append(count).append(" products from our database");
                    if (count >= 20) {
                        response.append(" (showing first 20)");
                    }
                    response.append(":\n\n");
                } else {
                    response.append("I found ").append(count).append(" product(s) matching '").append(query).append("':\n\n");
                }

                for (Map<String, Object> product : results) {
                    response.append("• ").append(product.get("name"));
                    if (product.get("category") != null) {
                        response.append(" (").append(product.get("category")).append(")");
                    }
                    response.append(" - ID: ").append(product.get("id"));
                    response.append("\n");
                }

                return response.toString().trim();
            }

            if ("tariff rate".equals(queryType)) {
                Boolean found = (Boolean) data.get("found");
                if (!found) {
                    return data.get("message").toString();
                }

                @SuppressWarnings("unchecked")
                List<Map<String, Object>> tariffs = (List<Map<String, Object>>) data.get("tariffs");
                int count = tariffs != null ? tariffs.size() : 0;

                // Check if user requested a specific product
                boolean productRequested = data.containsKey("productRequested") && (Boolean) data.get("productRequested");
                boolean productFound = data.containsKey("productFound") && (Boolean) data.get("productFound");
                String productQuery = data.containsKey("productQuery") ? data.get("productQuery").toString() : "";
                boolean specificProduct = data.containsKey("specificProduct") && (Boolean) data.get("specificProduct");

                StringBuilder response = new StringBuilder();

                // If product was requested but not found, show helpful message
                if (productRequested && !productFound) {
                    response.append("I couldn't find a product named '").append(productQuery).append("' in our database.\n\n");
                    if (count > 0) {
                        response.append("However, here are all tariff rates for ").append(data.get("route")).append(":\n\n");
                    }
                } else if (specificProduct) {
                    // User requested and found a specific product
                    response.append("Tariff rate for ").append(data.get("route"));
                    if (count == 1 && tariffs.get(0).get("product") != null) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> product = (Map<String, Object>) tariffs.get(0).get("product");
                        response.append(" (").append(product.get("name")).append(")");
                    }
                    response.append(":\n\n");
                } else {
                    // General query without specific product
                    response.append("Tariff rates for ").append(data.get("route")).append(":\n\n");
                }

                if (count == 0) {
                    return response.append("No tariff data available.").toString();
                }

                for (Map<String, Object> tariff : tariffs) {
                    if (tariff.get("product") != null) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> product = (Map<String, Object>) tariff.get("product");
                        response.append("**").append(product.get("name")).append("**\n");
                    }
                    response.append("• Base Rate: ").append(tariff.get("baseRate")).append("\n");
                    response.append("• Additional Fees: ").append(tariff.get("totalAdditionalFees")).append("\n");
                    response.append("• Effective Year: ").append(tariff.get("effectiveYear")).append("\n\n");
                }

                return response.toString().trim();
            }

        } catch (Exception e) {
            return "I found some information but had trouble formatting it. Error: " + e.getMessage();
        }

        return null;
    }

    /**
     * Removes markdown formatting (*, **, `) while preserving meaningful empty
     * lines and structure for readability.
     */
    private String cleanMarkdownFormatting(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // Remove bold (**text**)
        text = text.replaceAll("\\*\\*([^*]+)\\*\\*", "$1");

        // Remove italic (*text*)
        text = text.replaceAll("(?<!\\*)\\*([^*]+)\\*(?!\\*)", "$1");

        // Remove inline code (`text`)
        text = text.replaceAll("`([^`]+)`", "$1");

        // Remove code block markers (```language and ```)
        text = text.replaceAll("```[a-zA-Z]*\\n?", "");

        // Remove headers (### text -> text)
        text = text.replaceAll("^#{1,6}\\s+", "");
        text = text.replaceAll("\\n#{1,6}\\s+", "\n");

        // Clean up bullet points but keep structure
        // Replace * or - at start of line with simple dash
        text = text.replaceAll("(?m)^\\s*[*-]\\s+", "• ");

        // Preserve double newlines (paragraph breaks) but clean up excessive ones
        text = text.replaceAll("\\n{3,}", "\n\n");

        return text.trim();
    }
}
