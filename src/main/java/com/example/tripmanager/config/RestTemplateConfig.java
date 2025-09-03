package com.example.tripmanager.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    public static final String REST_BEAN_RABBITMQ_MANAGEMENT = "rabbitMQManagementRestTemplate";

    @Bean(REST_BEAN_RABBITMQ_MANAGEMENT)
    public RestTemplate rabbitMQManagementRestTemplate(
            @Value("${spring.rabbitmq.management.username}") String username,
            @Value("${spring.rabbitmq.management.password}") String password) {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplateBuilder()
                .requestFactory(() -> requestFactory)
                .basicAuthentication(username, password)
                .build();
    }
}
