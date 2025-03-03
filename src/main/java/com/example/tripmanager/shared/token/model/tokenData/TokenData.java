package com.example.tripmanager.shared.token.model.tokenData;

import com.example.tripmanager.shared.token.model.TokenType;

import java.util.Collections;
import java.util.Map;

public record TokenData(
        String accountId,
        long expirationMillis,
        TokenType tokenType,
        Map<String, String> additionalData
) {
    public TokenData(String accountId, long expirationMillis, TokenType tokenType, Map<String, String> additionalData) {
        this.accountId = accountId;
        this.expirationMillis = expirationMillis;
        this.tokenType = tokenType;
        this.additionalData = Collections.unmodifiableMap(additionalData);
    }
}