package com.example.tripmanager.monitoring.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitProps {
    private Exchange exchange = new Exchange();
    private Queue queue = new Queue();
    private Routing routing = new Routing();
    private Dlq dlq = new Dlq();

    public static class Exchange {
        private String default_;
        private List<String> allowlist = List.of();
        public String getDefault() { return default_; }
        public void setDefault(String v) { this.default_ = v; }
        public List<String> getAllowlist() { return allowlist; }
        public void setAllowlist(List<String> v) { this.allowlist = v; }

        public String getDefault_() {
            return default_;
        }

        public void setDefault_(String default_) {
            this.default_ = default_;
        }
    }
    public static class Queue {
        private String email;
        private List<String> allowlist = List.of();

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<String> getAllowlist() {
            return allowlist;
        }

        public void setAllowlist(List<String> allowlist) {
            this.allowlist = allowlist;
        }
    }
    public static class Routing {
        private String email;
        private List<String> allowlist = List.of();

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<String> getAllowlist() {
            return allowlist;
        }

        public void setAllowlist(List<String> allowlist) {
            this.allowlist = allowlist;
        }
    }
    public static class Dlq {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public Exchange getExchange() {
        return exchange;
    }

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public Routing getRouting() {
        return routing;
    }

    public void setRouting(Routing routing) {
        this.routing = routing;
    }

    public Dlq getDlq() {
        return dlq;
    }

    public void setDlq(Dlq dlq) {
        this.dlq = dlq;
    }
}

