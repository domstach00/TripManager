package com.example.tripmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    public static final String REST_BEAN_RABBITMQ_MANAGEMENT = "rabbitMQManagementRestTemplate";

    @Bean(RestTemplateConfig.REST_BEAN_RABBITMQ_MANAGEMENT)
    public RestTemplate rabbitMQManagementRestTemplate(
            RestTemplateBuilder builder,
            @Value("${spring.rabbitmq.management.username}") String username,
            @Value("${spring.rabbitmq.management.password}") String password) {
        return builder
                .basicAuthentication(username, password)
                .build();
    }
}