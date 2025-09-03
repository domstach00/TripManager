package com.example.tripmanager.monitoring.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public class RequeueMessageRequest {
    @NotBlank
    private String exchange;
    @NotBlank
    private String routingKey;
    private String payload;
    private Map<String, Object> properties;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}