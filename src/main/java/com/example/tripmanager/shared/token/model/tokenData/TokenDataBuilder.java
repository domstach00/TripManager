package com.example.tripmanager.shared.token.model.tokenData;

import com.example.tripmanager.shared.token.model.TokenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public interface TokenDataBuilder {
    TokenDataBuilder accountId(@NotBlank String accountId);
    TokenDataBuilder expirationDate(@NotNull LocalDateTime expirationDate);
    TokenDataBuilder expirationDate(long expirationDateMillis);
    TokenDataBuilder tokenType(@NotNull TokenType tokenType);
    TokenDataBuilder addAdditionalData(@NotBlank String key, @NotBlank String value);
    TokenDataBuilder addAdditionalData(@NotNull Map<String, String> additionalDataMap);
    TokenData build();
}
