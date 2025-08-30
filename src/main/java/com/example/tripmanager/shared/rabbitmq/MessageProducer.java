package com.example.tripmanager.shared.rabbitmq;

import com.example.tripmanager.email.model.EmailDetails;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "app.rabbit", name = "enabled", havingValue = "true")
public class MessageProducer {
    private static final Logger log = LoggerFactory.getLogger(MessageProducer.class);
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.default}")
    private String exchange;

    public MessageProducer(@NotNull RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Retryable(
            retryFor = AmqpException.class,
            maxAttempts = 5,
            backoff = @Backoff( delay = 5000 )
    )
    public void sendMessage(String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    @Recover
    void recoverFromAmqpException(AmqpException exception, String routingKey, Object message) {
        if (message instanceof EmailDetails emailDetails) {
            log.error("Recovering after failed attempts. Could not send email message to {}. routingKey={} messageClass={} message={}, exception={}",
                    emailDetails.getRecipient(), routingKey, emailDetails.getClass().getName(), emailDetails, exception);
        } else {
            log.error("Recovering after failed attempts. Could not send message. routingKey={} messageClass={} message={}, exception={}",
                    routingKey, message.getClass().getName(), message, exception);
        }
    }
}
