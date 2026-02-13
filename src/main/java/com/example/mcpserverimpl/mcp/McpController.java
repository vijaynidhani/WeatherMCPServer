package com.example.mcpserverimpl.mcp;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class McpController {

    private final McpRequestHandler requestHandler;

    public McpController(McpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @PostMapping(path = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRpcResponse handle(@RequestBody JsonRpcRequest request) {
        return requestHandler.handle(request);
    }
}
