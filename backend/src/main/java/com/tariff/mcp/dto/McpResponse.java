package com.tariff.mcp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MCP JSON-RPC 2.0 Response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class McpResponse {

    @JsonProperty("jsonrpc")
    private String jsonRpc = "2.0";

    private Object id;

    private Object result;

    private McpError error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class McpError {

        private int code;
        private String message;
        private Object data;
    }

    public static McpResponse success(Object id, Object result) {
        McpResponse response = new McpResponse();
        response.setId(id);
        response.setResult(result);
        return response;
    }

    public static McpResponse error(Object id, int code, String message) {
        McpResponse response = new McpResponse();
        response.setId(id);
        response.setError(new McpError(code, message, null));
        return response;
    }
}
