package com.example.tripmanager.email.service;

import com.example.tripmanager.email.model.EmailDetails;
import com.example.tripmanager.shared.rabbitmq.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final MessageProducer messageProducer;

    @Value("${rabbitmq.routing.email}")
    private String emailRoutingKey;

    @Autowired
    public EmailService(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    private void sendMessage(EmailDetails emailDetails) {
        messageProducer.sendMessage(emailRoutingKey, emailDetails);
    }
}
