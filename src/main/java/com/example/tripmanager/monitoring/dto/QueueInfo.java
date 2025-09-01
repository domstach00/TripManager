package com.example.tripmanager.monitoring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record QueueInfo(
    String name,
    String vhost,
    boolean durable,
    boolean autoDelete,
    @JsonProperty("messages") int messages,
    @JsonProperty("messages_ready") int messagesReady,
    @JsonProperty("messages_unacknowledged") int messagesUnacknowledged,
    @JsonProperty("consumers") int consumers,
    String node
) {}
