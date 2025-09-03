package com.example.tripmanager.monitoring.config;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RabbitAllowlist {
    private final Set<String> exchanges;
    private final Set<String> routingKeys;
    private final Set<String> queues;

    public RabbitAllowlist(RabbitProps p) {
        this.exchanges = new HashSet<>(p.getExchange().getAllowlist());
        if (p.getExchange().getDefault() != null)
            exchanges.add(p.getExchange().getDefault());

        this.routingKeys = new HashSet<>(p.getRouting().getAllowlist());
        if (p.getRouting().getEmail() != null)
            routingKeys.add(p.getRouting().getEmail());

        this.queues = new HashSet<>(p.getQueue().getAllowlist());
        if (p.getQueue().getEmail() != null)
            queues.add(p.getQueue().getEmail());
        if (p.getDlq().getName() != null)
            queues.add(p.getDlq().getName());
    }

    public void requireAllowedExchange(String exchange) {
        if (!exchanges.contains(exchange)) {
            throw new IllegalArgumentException("Exchange not allowed: " + exchange);
        }
    }
    public void requireAllowedRoutingKey(String rk) {
        if (!routingKeys.contains(rk)) {
            throw new IllegalArgumentException("Routing key not allowed: " + rk);
        }
    }
    public void requireAllowedQueue(String q) {
        if (!queues.contains(q)) {
            throw new IllegalArgumentException("Queue not allowed: " + q);
        }
    }
}

