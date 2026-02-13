package com.example.mcpserverimpl.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class McpContent {

    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

    public McpContent(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
}
