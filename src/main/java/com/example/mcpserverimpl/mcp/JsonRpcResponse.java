package com.example.mcpserverimpl.mcp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonRpcResponse {

    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("id")
    private Object id;

    @JsonProperty("result")
    private Object result;

    @JsonProperty("error")
    private JsonRpcError error;

    public JsonRpcResponse(Object id, Object result) {
        this.jsonrpc = "2.0";
        this.id = id;
        this.result = result;
    }

    public JsonRpcResponse(Object id, JsonRpcError error) {
        this.jsonrpc = "2.0";
        this.id = id;
        this.error = error;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public Object getId() {
        return id;
    }

    public Object getResult() {
        return result;
    }

    public JsonRpcError getError() {
        return error;
    }
}
