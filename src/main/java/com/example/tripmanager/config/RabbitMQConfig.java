package com.example.tripmanager.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app.rabbit", name = "enabled", havingValue = "true")
public class RabbitMQConfig {
    @Value("${rabbitmq.exchange.default}")
    private String defaultExchange;

    @Value("${rabbitmq.queue.email}")
    private String emailQueue;
    @Value("${rabbitmq.routing.email}")
    private String emailRoutingKey;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(defaultExchange, true, false);
    }

    // Queues
    @Bean
    public Queue emailQueue() {
        return new Queue(emailQueue, true);
    }

    // Binding – Binding the queue to the exchange and routing key
    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange exchange) {
        return BindingBuilder.bind(emailQueue).to(exchange).with(emailRoutingKey);
    }


    // Message to JSON converter (for object serialization)
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate configuration (with JSON serialization)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
