package com.tariff.mcp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

/**
 * MCP JSON-RPC 2.0 Request
 */
@Data
public class McpRequest {

    @JsonProperty("jsonrpc")
    private String jsonRpc = "2.0";

    private Object id;

    private String method;

    private Map<String, Object> params;
}
