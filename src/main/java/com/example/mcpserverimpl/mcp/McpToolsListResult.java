package com.example.mcpserverimpl.mcp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class McpToolsListResult {

    @JsonProperty("tools")
    private List<McpTool> tools;

    public McpToolsListResult(List<McpTool> tools) {
        this.tools = tools;
    }

    public List<McpTool> getTools() {
        return tools;
    }
}
