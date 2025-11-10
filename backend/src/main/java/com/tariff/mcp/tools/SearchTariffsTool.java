package com.tariff.mcp.tools;

import com.tariff.entity.Product;
import com.tariff.mcp.dto.ToolDefinition;
import com.tariff.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MCP Tool: Search products/tariffs by name or category Allows AI to query the
 * database for tariff information
 */
@Component
public class SearchTariffsTool implements McpTool {

    private final ProductService productService;

    public SearchTariffsTool(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ToolDefinition getDefinition() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        schema.put("properties", Map.of(
                "query", Map.of(
                        "type", "string",
                        "description", "Product name or description to search for"
                ),
                "limit", Map.of(
                        "type", "number",
                        "description", "Maximum number of results (default: 10)"
                )
        ));
        schema.put("required", List.of("query"));

        return new ToolDefinition(
                "search_tariffs",
                "Search for products and their tariff information by name or description. Returns matching products with their HS codes and categories.",
                schema
        );
    }

    @Override
    public Object execute(Map<String, Object> arguments) throws Exception {
        String query = (String) arguments.get("query");
        Integer limit = arguments.containsKey("limit")
                ? ((Number) arguments.get("limit")).intValue()
                : 10;

        // Get all products
        List<Product> allProducts = productService.listProduct();

        // If query is null or empty, return all products (up to limit)
        List<Product> matchingProducts;
        if (query == null || query.trim().isEmpty()) {
            matchingProducts = allProducts.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        } else {
            // Filter by query
            matchingProducts = allProducts.stream()
                    .filter(p -> p.getName().toLowerCase().contains(query.toLowerCase()))
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        // Format response
        List<Map<String, Object>> results = matchingProducts.stream()
                .map(product -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", product.getId());
                    result.put("name", product.getName());
                    result.put("category", product.getCategory() != null
                            ? product.getCategory().getName() : null);
                    return result;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("query", query);
        response.put("count", results.size());
        response.put("results", results);

        return response;
    }
}
