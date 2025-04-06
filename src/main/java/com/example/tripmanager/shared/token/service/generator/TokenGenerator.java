package com.example.tripmanager.shared.token.service.generator;

import com.example.tripmanager.shared.token.model.token.Token;
import com.example.tripmanager.shared.token.model.TokenType;

import java.util.Map;

public interface TokenGenerator<T extends Token> {
    T generate(String accountId, Map<String, Object> additionalData);
    TokenType getSupportedTokenType();
    Class<T> getTokenImplementationClass();
}
