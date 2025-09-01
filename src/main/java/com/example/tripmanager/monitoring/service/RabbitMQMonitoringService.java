package com.example.tripmanager.monitoring.service;

import com.example.tripmanager.config.RestTemplateConfig;
import com.example.tripmanager.monitoring.dto.QueueInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RabbitMQMonitoringService {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQMonitoringService.class);

    private final RestTemplate restTemplate;
    private final String rabbitMqManagementUrl;
    private final String vhost;

    public RabbitMQMonitoringService(@Qualifier(RestTemplateConfig.REST_BEAN_RABBITMQ_MANAGEMENT) RestTemplate restTemplate,
                                   @Value("${spring.rabbitmq.management.url}") String rabbitMqManagementUrl,
                                   @Value("${spring.rabbitmq.virtual-host}") String vhost) {
        this.restTemplate = restTemplate;
        this.rabbitMqManagementUrl = rabbitMqManagementUrl;
        this.vhost = vhost;
    }

    public List<QueueInfo> getQueues() {
        try {
            String encodedVhost = URLEncoder.encode(this.vhost, StandardCharsets.UTF_8);
            URI uri = new URI(String.format("%s/api/queues/%s", rabbitMqManagementUrl, encodedVhost));
            QueueInfo[] queues = restTemplate.getForObject(uri, QueueInfo[].class);
            return queues != null ? Arrays.asList(queues) : Collections.emptyList();
        } catch (URISyntaxException e) {
            log.error("Failed to construct RabbitMQ Management API URI. baseUrl='{}', vhost='{}'",
                    rabbitMqManagementUrl, vhost, e);
            throw new RuntimeException("Failed to construct RabbitMQ Management API URL", e);
        }
    }
}
