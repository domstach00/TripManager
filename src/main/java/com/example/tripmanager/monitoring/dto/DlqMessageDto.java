package com.example.tripmanager.monitoring.dto;

import java.util.Map;

public class DlqMessageDto {
    private Map<String, Object> properties;
    private String payload;

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
