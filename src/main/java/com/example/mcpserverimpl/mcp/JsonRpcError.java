package com.example.mcpserverimpl.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonRpcError {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    public JsonRpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
