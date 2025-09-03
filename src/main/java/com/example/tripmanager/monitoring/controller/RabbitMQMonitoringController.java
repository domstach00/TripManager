package com.example.tripmanager.monitoring.controller;

import com.example.tripmanager.account.model.Role;
import com.example.tripmanager.monitoring.dto.QueueInfo;
import com.example.tripmanager.monitoring.service.RabbitMQMonitoringService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import com.example.tripmanager.monitoring.dto.DlqMessageDto;
import com.example.tripmanager.monitoring.dto.RequeueMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/monitoring/rabbitmq")
@Secured(Role.ROLE_ADMIN_NAME)
public class RabbitMQMonitoringController {

    private final RabbitMQMonitoringService rabbitMQMonitoringService;

    public RabbitMQMonitoringController(RabbitMQMonitoringService rabbitMQMonitoringService) {
        this.rabbitMQMonitoringService = rabbitMQMonitoringService;
    }

    @GetMapping("/queues")
    public ResponseEntity<List<QueueInfo>> getQueues() {
        List<QueueInfo> queues = rabbitMQMonitoringService.getQueues();
        return ResponseEntity.ok(queues);
    }

    @GetMapping("/dlq")
    public ResponseEntity<List<DlqMessageDto>> getDlqMessages() {
        List<DlqMessageDto> messages = rabbitMQMonitoringService.getDlqMessages();
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/requeue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requeueMessage(@RequestBody RequeueMessageRequest request) {
        rabbitMQMonitoringService.requeueMessage(request);
        rabbitMQMonitoringService.deleteExactMessageFromDlq(
                request.getPayload(),
                Optional.ofNullable(request.getProperties())
                        .map(props -> Objects.toString(props.get("correlation_id"), null))
                        .orElse(null)
        );
    }
}
