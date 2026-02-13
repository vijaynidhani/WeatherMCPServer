# MCP Server - Abu Dhabi Weather

This Spring Boot service exposes current weather data for Abu Dhabi via REST and MCP endpoints.

## Run

```bash
mvn spring-boot:run
```

## Docker

Build the image:

```bash
docker build -t mcp-abu-dhabi-weather .
```

Run the container (exposes port 8080):

```bash
docker run --rm -p 8080:8080 mcp-abu-dhabi-weather
```

## Docker Compose

```bash
docker compose up --build
```

## REST endpoint

- GET /api/weather/abu-dhabi

## MCP endpoint

- POST /mcp

Example request body:

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "tools/list"
}
```

Tool call example:

```json
{
  "jsonrpc": "2.0",
  "id": 2,
  "method": "tools/call",
  "params": {
    "name": "get_current_weather_abu_dhabi",
    "arguments": {}
  }
}
```

## Curl tests

List tools:

```bash
curl -s -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":1,"method":"tools/list"}'
```

Successful response example:

```json
{"jsonrpc":"2.0","id":1,"result":{"tools":[{"name":"get_current_weather_abu_dhabi","description":"Get the current weather for Abu Dhabi.","inputSchema":{"additionalProperties":false,"properties":{},"type":"object"}}]}}
```

Call the weather tool:

```bash
curl -s -X POST http://localhost:8080/mcp \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":2,"method":"tools/call","params":{"name":"get_current_weather_abu_dhabi","arguments":{}}}'
```

Successful response example:

```json
{"jsonrpc":"2.0","id":2,"result":{"content":[{"type":"text","text":"Abu Dhabi weather: 24.9C, wind 1.9 km/h, code 0, observed at 2026-02-13T06:31:59.093407626Z"}]}}
```
