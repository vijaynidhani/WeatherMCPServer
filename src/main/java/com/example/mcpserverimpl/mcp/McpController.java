package com.example.mcpserverimpl.mcp;

import com.example.mcpserverimpl.model.WeatherInfo;
import com.example.mcpserverimpl.service.WeatherService;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class McpController {

    private static final String TOOL_NAME = "get_current_weather_abu_dhabi";
    private static final Logger logger = LoggerFactory.getLogger(McpController.class);

    private final WeatherService weatherService;

    public McpController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @PostMapping(path = "/mcp", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JsonRpcResponse handle(@RequestBody JsonRpcRequest request) {
        if (request == null || request.getMethod() == null) {
            logger.warn("MCP request missing method");
            return new JsonRpcResponse(null, new JsonRpcError(-32600, "Invalid Request"));
        }

        logger.info("MCP request received: method={}, id={}", request.getMethod(), request.getId());

        if ("tools/list".equals(request.getMethod())) {
            logger.debug("MCP tools list requested");
            return new JsonRpcResponse(request.getId(), new McpToolsListResult(List.of(buildToolDefinition())));
        }

        if ("tools/call".equals(request.getMethod())) {
            return handleToolCall(request);
        }

        logger.warn("MCP method not found: method={}", request.getMethod());
        return new JsonRpcResponse(request.getId(), new JsonRpcError(-32601, "Method not found"));
    }

    private JsonRpcResponse handleToolCall(JsonRpcRequest request) {
        if (!(request.getParams() instanceof Map<?, ?> paramsMap)) {
            logger.warn("MCP tool call with invalid params: id={}", request.getId());
            return new JsonRpcResponse(request.getId(), new JsonRpcError(-32602, "Invalid params"));
        }

        McpToolCallParams params = new McpToolCallParams();
        Object nameValue = paramsMap.get("name");
        if (nameValue instanceof String nameString) {
            params.setName(nameString);
        }

        if (!TOOL_NAME.equals(params.getName())) {
            logger.warn("MCP unknown tool requested: name={}, id={}", params.getName(), request.getId());
            return new JsonRpcResponse(request.getId(), new JsonRpcError(-32601, "Unknown tool"));
        }

        logger.info("MCP tool execution started: name={}, id={}", params.getName(), request.getId());
        WeatherInfo weatherInfo = weatherService.getCurrentWeather();
        String summary = "Abu Dhabi weather: " + weatherInfo.temperatureCelsius() + "C, wind "
                + weatherInfo.windSpeedKph() + " km/h, code " + weatherInfo.weatherCode()
                + ", observed at " + weatherInfo.observedAt();

        logger.info("MCP tool execution completed: name={}, id={}", params.getName(), request.getId());
        McpToolResult result = new McpToolResult(List.of(new McpContent("text", summary)));
        return new JsonRpcResponse(request.getId(), result);
    }

    private McpTool buildToolDefinition() {
        Map<String, Object> inputSchema = Map.of(
                "type", "object",
                "properties", Map.of(),
                "additionalProperties", false
        );

        return new McpTool(
                TOOL_NAME,
                "Get the current weather for Abu Dhabi.",
                inputSchema
        );
    }
}
