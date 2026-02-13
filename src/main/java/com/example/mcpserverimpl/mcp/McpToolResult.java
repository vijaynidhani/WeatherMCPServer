package com.example.mcpserverimpl.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class McpToolResult {

    @JsonProperty("content")
    private List<McpContent> content;

    public McpToolResult(List<McpContent> content) {
        this.content = content;
    }

    public List<McpContent> getContent() {
        return content;
    }
}
