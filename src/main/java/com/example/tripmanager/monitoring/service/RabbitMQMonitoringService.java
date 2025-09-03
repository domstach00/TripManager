package com.example.tripmanager.monitoring.service;

import com.example.tripmanager.config.RestTemplateConfig;
import com.example.tripmanager.monitoring.config.RabbitAllowlist;
import com.example.tripmanager.monitoring.config.RabbitProps;
import com.example.tripmanager.monitoring.dto.DlqMessageDto;
import com.example.tripmanager.monitoring.dto.QueueInfo;
import com.example.tripmanager.monitoring.dto.RequeueMessageRequest;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RabbitMQMonitoringService {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQMonitoringService.class);

    private final RestTemplate restTemplate;
    private final String rabbitMqManagementUrl;
    private final String vhost;
    private final String dlqName;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAllowlist rabbitAllowlist;
    private final RabbitProps rabbitProps;

    public RabbitMQMonitoringService(@Qualifier(RestTemplateConfig.REST_BEAN_RABBITMQ_MANAGEMENT) RestTemplate restTemplate,
                                     @Value("${spring.rabbitmq.management.url}") String rabbitMqManagementUrl,
                                     @Value("${spring.rabbitmq.virtual-host}") String vhost,
                                     @Value("${rabbitmq.dlq.name}") String dlqName,
                                     RabbitTemplate rabbitTemplate,
                                     RabbitAllowlist rabbitAllowlist,
                                     RabbitProps rabbitProps) {
        this.restTemplate = restTemplate;
        this.rabbitMqManagementUrl = rabbitMqManagementUrl;
        this.vhost = vhost;
        this.dlqName = dlqName;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAllowlist = rabbitAllowlist;
        this.rabbitProps = rabbitProps;
    }

    public List<QueueInfo> getQueues() {
        URI uri = UriComponentsBuilder.fromUriString(rabbitMqManagementUrl)
                .path("/api/queues/{vhost}")
                .build(vhost);
        QueueInfo[] queues = restTemplate.getForObject(uri, QueueInfo[].class);
        return queues != null ? Arrays.asList(queues) : Collections.emptyList();
    }

    public List<DlqMessageDto> getDlqMessages() {
        try {
            URI uri = UriComponentsBuilder.fromUriString(rabbitMqManagementUrl)
                    .path("/api/queues/{vhost}/{queue}/get")
                    .build(vhost, dlqName);

            Map<String, Object> body = new HashMap<>();
            body.put("count", 10);
            body.put("ackmode", "ack_requeue_true");
            body.put("encoding", "auto");

            DlqMessageDto[] messages = restTemplate.postForObject(uri, body, DlqMessageDto[].class);
            return messages != null ? Arrays.asList(messages) : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            log.error("Error while fetching DLQ messages: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void requeueMessage(@NonNull RequeueMessageRequest request) {
        Objects.requireNonNull(request.getRoutingKey(), "routingKey is required");
        Objects.requireNonNull(request.getExchange(),   "exchange is required");

        String exchange = (request.getExchange() == null || request.getExchange().isBlank())
                ? rabbitProps.getExchange().getDefault()
                : request.getExchange();
        rabbitAllowlist.requireAllowedExchange(exchange);
        rabbitAllowlist.requireAllowedRoutingKey(request.getRoutingKey());

        Map<String, Object> props = request.getProperties() != null
                ? new HashMap<>(request.getProperties())
                : new HashMap<>();
        Object headers = props.get("headers");
        if (headers instanceof Map<?, ?> hmap) {
            hmap.remove("x-death");
            hmap.remove("x-first-death-exchange");
            hmap.remove("x-first-death-queue");
            hmap.remove("x-first-death-reason");
        }

        try {
            URI uri = UriComponentsBuilder.fromUriString(rabbitMqManagementUrl)
                    .path("/api/exchanges/{vhost}/{exchange}/publish")
                    .build(vhost, exchange);

            Map<String, Object> body = new HashMap<>();
            body.put("routing_key", request.getRoutingKey());
            body.put("payload", request.getPayload());
            body.put("payload_encoding", "string");
            body.put("properties", props);
            body.put("mandatory", Boolean.TRUE);

            HttpHeaders headersHttp = new HttpHeaders();
            headersHttp.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headersHttp);

            @SuppressWarnings("unchecked")
            Map<String, Object> resp = restTemplate.postForObject(uri, entity, Map.class);

            if (resp != null && Boolean.FALSE.equals(resp.get("routed"))) {
                throw new IllegalStateException("Publish not routed (check bindings/routing key)");
            }
        } catch (HttpClientErrorException e) {
            log.error("Error while requeueing message: {}", e.getResponseBodyAsString());
            throw new RuntimeException("Failed to requeue message", e);
        }
    }

    /**
     * Removes exactly one message from the DLQ that meets the condition (payload == targetPayload
     * and – if specified – correlationId == targetCorrelationId). Restores the rest in the same order.
     */
    public void deleteExactMessageFromDlq(@NonNull String targetPayload, String targetCorrelationId) {
        boolean removed = Boolean.TRUE.equals(rabbitTemplate.execute(channel -> {
            channel.basicQos(0); // bez limitu prefetch na basic.get
            final List<GetResponse> all = new java.util.ArrayList<>();

            // Remove ALL DLQ (manual ack)
            while (true) {
                GetResponse gr = channel.basicGet(dlqName, false); // autoAck=false
                if (gr == null) break;
                // acking photo from DLQ – rebuild the queue below
                channel.basicAck(gr.getEnvelope().getDeliveryTag(), false);
                all.add(gr);
            }

            // Find a message to delete
            int removeIdx = -1;
            for (int i = 0; i < all.size(); i++) {
                GetResponse gr = all.get(i);
                String body = new String(gr.getBody(), StandardCharsets.UTF_8);
                AMQP.BasicProperties props = gr.getProps();
                String corrId = props == null ? null : props.getCorrelationId();

                boolean match = body.equals(targetPayload)
                        && (targetCorrelationId == null || Objects.equals(corrId, targetCorrelationId));
                if (match) {
                    removeIdx = i;
                    break;
                }
            }

            // Open DLQ without this one message (in order)
            for (int i = 0; i < all.size(); i++) {
                if (i == removeIdx)
                    continue;
                GetResponse gr = all.get(i);
                AMQP.BasicProperties props = gr.getProps();
                byte[] body = gr.getBody();
                // publish back to DLQ
                channel.basicPublish("", dlqName, true, props, body);
            }
            return removeIdx >= 0;
        }));

        if (!removed) {
            log.info("No matching message found in DLQ [{}] for payload='{}' and correlationId='{}'",
                    dlqName, targetPayload, targetCorrelationId);
        } else {
            log.info("Removed one matching message from DLQ [{}] with payload='{}' and correlationId='{}'",
                    dlqName, targetPayload, targetCorrelationId);
        }
    }

}
