package com.example.tripmanager.shared.token.service.generator;

import com.example.tripmanager.shared.token.model.token.AccountActivationToken;
import com.example.tripmanager.shared.token.model.TokenType;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Token generator for account activation tokens.
 */
@Component
public class AccountActivationTokenGenerator extends AbstractTokenGenerator<AccountActivationToken> {

    /**
     * Generates an AccountActivationToken.
     *
     * @param accountId      ID of the account generating the token
     * @param additionalData Not used for this token type
     * @return the generated AccountActivationToken
     */
    @Override
    public AccountActivationToken generate(String accountId, Map<String, Object> additionalData) {
        return generateTokenWithBasicValues(accountId);
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.ACCOUNT_ACTIVATION;
    }

    @Override
    public Class<AccountActivationToken> getTokenImplementationClass() {
        return AccountActivationToken.class;
    }
}
