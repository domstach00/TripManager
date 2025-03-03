package com.example.tripmanager.shared.token.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "token.expiration")
public class TokenConfiguration {
    private String secret;
    private Duration accountActivation;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getAccountActivation() {
        return accountActivation;
    }

    public void setAccountActivation(Duration accountActivation) {
        this.accountActivation = accountActivation;
    }
}
