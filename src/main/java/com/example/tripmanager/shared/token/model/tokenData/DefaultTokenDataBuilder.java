package com.example.tripmanager.shared.token.model.tokenData;

import com.example.tripmanager.shared.token.model.TokenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class DefaultTokenDataBuilder implements TokenDataBuilder {
    private String accountId;
    private long expirationMillis;
    private TokenType tokenType;
    private final Map<String, String> additionalData = new HashMap<>();

    @Override
    public TokenDataBuilder accountId(@NotBlank String accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public TokenDataBuilder expirationDate(@NotNull LocalDateTime expirationDate) {
        this.expirationMillis = expirationDate.atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        return this;
    }

    @Override
    public TokenDataBuilder expirationDate(long expirationDateMillis) {
        this.expirationMillis = expirationDateMillis;
        return this;
    }

    @Override
    public TokenDataBuilder tokenType(@NotNull TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    @Override
    public TokenDataBuilder addAdditionalData(@NotBlank String key, @NotBlank String value) {
        this.additionalData.put(key, value);
        return this;
    }

    @Override
    public TokenDataBuilder addAdditionalData(Map<String, String> additionalDataMap) {
        this.additionalData.putAll(additionalDataMap);
        return this;
    }

    @Override
    public TokenData build() {
        return new TokenData(accountId, expirationMillis, tokenType, additionalData);
    }
}
