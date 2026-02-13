package com.example.mcpserverimpl.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class McpExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(McpExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<JsonRpcResponse> handleInvalidJson(HttpMessageNotReadableException ex) {
        logger.info("MCP request rejected due to invalid JSON");
        JsonRpcError error = new JsonRpcError(-32700, "Parse error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonRpcResponse(null, error));
    }
}
