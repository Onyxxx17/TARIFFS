package com.tariff.mcp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Represents an MCP Tool definition
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolDefinition {

    private String name;
    private String description;
    private Map<String, Object> inputSchema;

    public String getName() {
        return name;
    }
}
