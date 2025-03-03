package com.example.tripmanager.email.rabbitmqConsumer;

import com.example.tripmanager.email.model.EmailDetails;
import com.example.tripmanager.email.service.EmailSenderService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);
    private final EmailSenderService emailSenderService;

    @Autowired
    public EmailConsumer(@NotNull EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.email}")
    public void receiveEmailMessage(EmailDetails emailDetails) {
        log.info("Received RabbitMQ email message: {}", emailDetails);
        emailSenderService.sendEmail(emailDetails);
    }
}
