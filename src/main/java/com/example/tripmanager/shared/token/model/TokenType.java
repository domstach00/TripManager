package com.example.tripmanager.shared.token.model;

import com.example.tripmanager.shared.token.config.TokenConfiguration;
import com.example.tripmanager.shared.token.model.token.AccountActivationToken;
import com.example.tripmanager.shared.token.model.token.BudgetInvitationToken;
import com.example.tripmanager.shared.token.model.token.Token;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public enum TokenType {
    ACCOUNT_ACTIVATION(Duration.ofHours(24), AccountActivationToken.class),
    BUDGET_INVITATION(Duration.ofDays(30),BudgetInvitationToken.class),
    ;

    private final Duration defaultDuration;
    private Duration configuredDuration;
    private final Class<? extends Token> associatedTokenClass;
    private static final Map<TokenType, Class<? extends Token>> tokenMap = Map.of(
            TokenType.ACCOUNT_ACTIVATION, AccountActivationToken.class,
            TokenType.BUDGET_INVITATION, BudgetInvitationToken.class
    );

    TokenType(Duration defaultDuration, Class<? extends Token> associatedTokenClass) {
        this.defaultDuration = defaultDuration;
        this.configuredDuration = null; // set from the TokenConfiguration
        this.associatedTokenClass = associatedTokenClass;
    }

    public LocalDateTime calculateExpiration(LocalDateTime creationDateTime) {
        if (creationDateTime == null) {
            return null;
        }
        Duration durationToUse = (configuredDuration != null)
                ? configuredDuration
                : defaultDuration;
        return creationDateTime.plus(durationToUse);
    }
    public LocalDateTime calculateExpiration() {
        return calculateExpiration(
                LocalDateTime.now()
        );
    }

    public void setConfiguredDuration(Duration configuredDuration) {
        this.configuredDuration = configuredDuration;
    }

    public Class<? extends Token> getAssociatedTokenClass() {
        Class<? extends Token> clazz = tokenMap.get(this);
        return clazz == null
                ? Token.class
                : clazz;
    }

    public static void applyConfiguration(TokenConfiguration tokenConfiguration) {
        ACCOUNT_ACTIVATION.setConfiguredDuration(tokenConfiguration.getAccountActivation());
    }
}
