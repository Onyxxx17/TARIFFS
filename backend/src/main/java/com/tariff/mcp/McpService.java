package com.tariff.mcp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tariff.mcp.dto.McpRequest;
import com.tariff.mcp.dto.McpResponse;
import com.tariff.mcp.dto.ToolDefinition;
import com.tariff.mcp.tools.McpTool;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MCP Server Service Handles tool registration and execution
 */
@Service
public class McpService {

    private final List<McpTool> tools;
    private final ObjectMapper objectMapper;

    public McpService(List<McpTool> tools, ObjectMapper objectMapper) {
        this.tools = tools;
        this.objectMapper = objectMapper;
    }

    /**
     * Handle incoming MCP request
     */
    public McpResponse handleRequest(McpRequest request) {
        try {
            String method = request.getMethod();

            switch (method) {
                case "tools/list":
                    return handleToolsList(request.getId());

                case "tools/call":
                    return handleToolCall(request.getId(), request.getParams());

                default:
                    return McpResponse.error(request.getId(), -32601,
                            "Method not found: " + method);
            }
        } catch (Exception e) {
            return McpResponse.error(request.getId(), -32603,
                    "Internal error: " + e.getMessage());
        }
    }

    /**
     * List all available tools
     */
    private McpResponse handleToolsList(Object id) {
        List<ToolDefinition> toolDefinitions = tools.stream()
                .map(McpTool::getDefinition)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("tools", toolDefinitions);

        return McpResponse.success(id, result);
    }

    /**
     * Execute a specific tool
     */
    private McpResponse handleToolCall(Object id, Map<String, Object> params) {
        try {
            String toolName = (String) params.get("name");
            @SuppressWarnings("unchecked")
            Map<String, Object> arguments = (Map<String, Object>) params.get("arguments");

            // Find the tool
            McpTool tool = tools.stream()
                    .filter(t -> t.getDefinition().getName().equals(toolName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Tool not found: " + toolName));

            // Execute the tool
            Object toolResult = tool.execute(arguments != null ? arguments : new HashMap<>());

            // Format response
            Map<String, Object> result = new HashMap<>();
            result.put("content", List.of(
                    Map.of(
                            "type", "text",
                            "text", objectMapper.writeValueAsString(toolResult)
                    )
            ));

            return McpResponse.success(id, result);

        } catch (Exception e) {
            return McpResponse.error(id, -32602,
                    "Tool execution failed: " + e.getMessage());
        }
    }

    /**
     * Get list of available tool names (for logging/debugging)
     */
    public List<String> getAvailableTools() {
        return tools.stream()
                .map(t -> t.getDefinition().getName())
                .collect(Collectors.toList());
    }
}
