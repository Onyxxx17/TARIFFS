package com.tariff.mcp.tools;

import com.tariff.entity.Country;
import com.tariff.entity.TariffRule;
import com.tariff.mcp.dto.ToolDefinition;
import com.tariff.repository.TariffRuleRepository;
import com.tariff.service.CountryService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MCP Tool: Get tariff rate information for a specific route Fetches tariff
 * rates and additional fees from the database
 */
@Component
public class GetTariffRateTool implements McpTool {

    private final TariffRuleRepository tariffRuleRepository;
    private final CountryService countryService;

    public GetTariffRateTool(TariffRuleRepository tariffRuleRepository, CountryService countryService) {
        this.tariffRuleRepository = tariffRuleRepository;
        this.countryService = countryService;
    }

    @Override
    public ToolDefinition getDefinition() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        schema.put("properties", Map.of(
                "fromCountry", Map.of(
                        "type", "string",
                        "description", "Country code or name of origin country (e.g., 'US', 'CN', 'United States')"
                ),
                "toCountry", Map.of(
                        "type", "string",
                        "description", "Country code or name of destination country (e.g., 'US', 'PH', 'Philippines')"
                ),
                "productId", Map.of(
                        "type", "number",
                        "description", "Product ID to get tariff rate for (optional, if omitted returns all rates for the route)"
                ),
                "effectiveYear", Map.of(
                        "type", "number",
                        "description", "Year for tariff rate (optional, defaults to current year)"
                )
        ));
        schema.put("required", List.of("fromCountry", "toCountry"));

        return new ToolDefinition(
                "get_tariff_rate",
                "Get tariff rates and additional fees for importing products between countries. Returns base tariff rate, additional fees, and effective year.",
                schema
        );
    }

    @Override
    public Object execute(Map<String, Object> arguments) throws Exception {
        String fromCountryInput = (String) arguments.get("fromCountry");
        String toCountryInput = (String) arguments.get("toCountry");
        Long productId = arguments.containsKey("productId")
                ? ((Number) arguments.get("productId")).longValue()
                : null;
        Integer effectiveYear = arguments.containsKey("effectiveYear")
                ? ((Number) arguments.get("effectiveYear")).intValue()
                : java.time.Year.now().getValue();

        if (fromCountryInput == null || fromCountryInput.trim().isEmpty()) {
            throw new IllegalArgumentException("fromCountry parameter is required");
        }
        if (toCountryInput == null || toCountryInput.trim().isEmpty()) {
            throw new IllegalArgumentException("toCountry parameter is required");
        }

        // Try to resolve country codes from names or codes
        String fromCountryCode = resolveCountryCode(fromCountryInput);
        String toCountryCode = resolveCountryCode(toCountryInput);

        if (fromCountryCode == null) {
            return Map.of(
                    "found", false,
                    "error", "Country not found: " + fromCountryInput,
                    "message", "Country not found: " + fromCountryInput + ". Please use valid country code (e.g., 'US', 'CN') or country name"
            );
        }
        if (toCountryCode == null) {
            return Map.of(
                    "found", false,
                    "error", "Country not found: " + toCountryInput,
                    "message", "Country not found: " + toCountryInput + ". Please use valid country code (e.g., 'US', 'PH') or country name"
            );
        }

        // Query tariff rules - Try to find rules where from_country matches OR is NULL (general tariffs)
        List<TariffRule> rules;
        if (productId != null) {
            // Get all tariff rules for this destination country and product
            org.springframework.data.domain.Page<TariffRule> page
                    = tariffRuleRepository.findByToCountryCountryCodeAndProductId(
                            toCountryCode, productId,
                            org.springframework.data.domain.Pageable.unpaged()
                    );
            List<TariffRule> allRules = page.getContent();

            // Filter: prefer specific from_country match, fallback to NULL (general) from_country
            rules = allRules.stream()
                    .filter(r -> {
                        // Match specific from_country OR from_country is NULL (general tariff)
                        return (r.getFromCountry() != null
                                && r.getFromCountry().getCountryCode().equals(fromCountryCode))
                                || r.getFromCountry() == null;
                    })
                    .collect(Collectors.toList());

            // Filter by effective year if specified
            if (effectiveYear != null) {
                final int year = effectiveYear;
                rules = rules.stream()
                        .filter(r -> r.getEffectiveYear() == year)
                        .collect(Collectors.toList());
            }
        } else {
            // Get all tariff rules for this destination country
            org.springframework.data.domain.Page<TariffRule> page
                    = tariffRuleRepository.findByToCountryCountryCode(
                            toCountryCode,
                            org.springframework.data.domain.Pageable.unpaged()
                    );
            List<TariffRule> allRules = page.getContent();

            // Filter by from_country (specific or NULL)
            rules = allRules.stream()
                    .filter(r -> {
                        return (r.getFromCountry() != null
                                && r.getFromCountry().getCountryCode().equals(fromCountryCode))
                                || r.getFromCountry() == null;
                    })
                    .collect(Collectors.toList());

            // Filter by effective year if specified
            if (effectiveYear != null) {
                final int year = effectiveYear;
                rules = rules.stream()
                        .filter(r -> r.getEffectiveYear() == year)
                        .collect(Collectors.toList());
            }
        }

        if (rules.isEmpty()) {
            // Build helpful error message
            String errorMsg;
            if (productId != null) {
                errorMsg = String.format(
                        "No tariff rates found for importing to %s (code: %s) in year %d. "
                        + "The product may not have tariff data, or the countries/year combination doesn't exist in our database.",
                        toCountryInput, toCountryCode, effectiveYear
                );
            } else {
                errorMsg = String.format(
                        "No tariff rates found for route %s → %s in year %d. "
                        + "This trade route may not have tariff data in our database.",
                        fromCountryInput, toCountryInput, effectiveYear
                );
            }

            return Map.of(
                    "found", false,
                    "message", errorMsg,
                    "fromCountry", fromCountryCode,
                    "toCountry", toCountryCode,
                    "effectiveYear", effectiveYear,
                    "productId", productId != null ? productId : "all",
                    "suggestion", "Try asking 'What products do you have?' or 'Which countries have tariff data?'"
            );
        }

        // Format response
        List<Map<String, Object>> tariffData = rules.stream()
                .map(rule -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", rule.getId());
                    data.put("baseRate", rule.getRate().toString() + "%");
                    data.put("additionalFees", rule.getAdditionalFees().stream()
                            .map(fee -> fee.toString() + "%")
                            .collect(Collectors.toList()));
                    data.put("totalAdditionalFees", rule.getAdditionalFees().stream()
                            .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add).toString() + "%");
                    data.put("effectiveYear", rule.getEffectiveYear());

                    if (rule.getProduct() != null) {
                        data.put("product", Map.of(
                                "id", rule.getProduct().getId(),
                                "name", rule.getProduct().getName(),
                                "category", rule.getProduct().getCategory() != null
                                ? rule.getProduct().getCategory().getName()
                                : "Unknown"
                        ));
                    }

                    return data;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("found", true);
        response.put("route", fromCountryInput + " → " + toCountryInput);
        response.put("fromCountry", fromCountryCode);
        response.put("toCountry", toCountryCode);
        response.put("effectiveYear", effectiveYear);
        response.put("count", tariffData.size());
        response.put("tariffs", tariffData);

        // Pass through metadata from GeminiService for better formatting
        if (arguments.containsKey("_productRequested")) {
            response.put("productRequested", arguments.get("_productRequested"));
        }
        if (arguments.containsKey("_productFound")) {
            response.put("productFound", arguments.get("_productFound"));
        }
        if (arguments.containsKey("_productQuery")) {
            response.put("productQuery", arguments.get("_productQuery"));
        }
        response.put("specificProduct", productId != null);

        return response;
    }

    /**
     * Resolve country code from name or code
     */
    private String resolveCountryCode(String input) {
        try {
            // First try as country code
            var country = countryService.getCountry(input.toUpperCase());
            if (country != null) {
                return country.getCountryCode();
            }
        } catch (Exception e) {
            // Not a valid code, try name
        }

        try {
            // Try as country name
            var countryOpt = countryService.getCountryByName(input);
            if (countryOpt.isPresent()) {
                return countryOpt.get().getCountryCode();
            }
        } catch (Exception e) {
            // Not found
        }

        return null;
    }
}
