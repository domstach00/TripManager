package com.example.tripmanager.shared.token.service.generator;

import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.token.PasswordResetToken;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Token generator for password reset tokens.
 */
@Component
public class PasswordResetTokenGenerator extends AbstractTokenGenerator<PasswordResetToken> {

    /**
     * Generates an PasswordResetToken.
     *
     * @param accountId      ID of the account generating the token
     * @param additionalData Not used for this token type
     * @return the generated PasswordResetToken
     */
    @Override
    public PasswordResetToken generate(String accountId, Map<String, Object> additionalData) {
        return generateTokenWithBasicValues(accountId);
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.PASSWORD_RESET;
    }

    @Override
    public Class<PasswordResetToken> getTokenImplementationClass() {
        return PasswordResetToken.class;
    }
}
