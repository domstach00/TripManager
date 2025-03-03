package com.example.tripmanager.shared.token.service;

import com.example.tripmanager.shared.token.exception.TokenValidationException;
import com.example.tripmanager.shared.token.model.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.tokenData.TokenData;
import com.example.tripmanager.shared.token.model.tokenData.TokenDataBuilder;

public interface TokenDataStrategy {
    TokenDataBuilder createTokenData() throws TokenValidationException;
    String serializeAndSign(TokenData data, String secret) throws TokenValidationException;
    TokenData deserializeAndVerify(String tokenValue, String secret) throws TokenValidationException;
    boolean validate(TokenData data, Token storedToken, TokenType expectedType);
}
