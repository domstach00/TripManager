package com.example.tripmanager.monitoring.controller;

import com.example.tripmanager.account.model.Role;
import com.example.tripmanager.monitoring.dto.QueueInfo;
import com.example.tripmanager.monitoring.service.RabbitMQMonitoringService;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
