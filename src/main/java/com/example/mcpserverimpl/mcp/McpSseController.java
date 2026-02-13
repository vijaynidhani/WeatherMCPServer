package com.example.mcpserverimpl.mcp;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/mcp")
public class McpSseController {

    private static final Logger logger = LoggerFactory.getLogger(McpSseController.class);
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    private final McpRequestHandler requestHandler;

    public McpSseController(McpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        String sessionId = UUID.randomUUID().toString();
        SseEmitter emitter = new SseEmitter(0L);
        emitters.put(sessionId, emitter);

        emitter.onCompletion(() -> emitters.remove(sessionId));
        emitter.onTimeout(() -> {
            emitters.remove(sessionId);
            emitter.complete();
        });

        String endpoint = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/mcp/messages")
                .queryParam("sessionId", sessionId)
                .toUriString();

        try {
            emitter.send(SseEmitter.event().name("endpoint").data(endpoint));
            logger.info("MCP SSE session established: sessionId={}", sessionId);
        } catch (IOException ex) {
            logger.warn("Failed to initialize MCP SSE session: sessionId={}", sessionId);
            emitters.remove(sessionId);
            emitter.completeWithError(ex);
        }

        return emitter;
    }

    @PostMapping(path = "/messages", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> handleMessage(
            @RequestParam("sessionId") String sessionId,
            @RequestBody JsonRpcRequest request) {
        SseEmitter emitter = emitters.get(sessionId);
        if (emitter == null) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }

        JsonRpcResponse response = requestHandler.handle(request);
        try {
            emitter.send(SseEmitter.event().name("message").data(response));
        } catch (IOException ex) {
            logger.warn("Failed to send MCP SSE response: sessionId={}", sessionId);
            emitters.remove(sessionId);
            emitter.completeWithError(ex);
            return ResponseEntity.status(HttpStatus.GONE).build();
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
