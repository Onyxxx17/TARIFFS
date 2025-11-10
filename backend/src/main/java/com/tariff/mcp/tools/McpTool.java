package com.tariff.mcp.tools;

import com.tariff.mcp.dto.ToolDefinition;
import java.util.Map;

/**
 * Interface for all MCP Tools Each tool provides database access to AI agents
 */
public interface McpTool {

    /**
     * Get the tool's definition (name, description, parameters)
     */
    ToolDefinition getDefinition();

    /**
     * Execute the tool with given arguments
     *
     * @param arguments Arguments from AI agent
     * @return Result object (will be serialized to JSON)
     */
    Object execute(Map<String, Object> arguments) throws Exception;
}
