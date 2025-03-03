package com.example.tripmanager.shared.token.model;

import com.example.tripmanager.shared.token.config.TokenConfiguration;

import java.time.Duration;
import java.time.LocalDateTime;

public enum TokenType {
    ACCOUNT_ACTIVATION(Duration.ofHours(24)),
    CUSTOM_DEFAULT_TOKEN(Duration.ofHours(24)),
    ;

    private final Duration defaultDuration;
    private Duration configuredDuration;

    TokenType(Duration defaultDuration) {
        this.defaultDuration = defaultDuration;
        this.configuredDuration = null; // set from the TokenConfiguration
    }

    public LocalDateTime calculateExpiration() {
        Duration durationToUse = (configuredDuration != null)
                ? configuredDuration
                : defaultDuration;
        return LocalDateTime.now().plus(durationToUse);
    }

    public void setConfiguredDuration(Duration configuredDuration) {
        this.configuredDuration = configuredDuration;
    }

    public static void applyConfiguration(TokenConfiguration tokenConfiguration) {
        ACCOUNT_ACTIVATION.setConfiguredDuration(tokenConfiguration.getAccountActivation());
    }
}
