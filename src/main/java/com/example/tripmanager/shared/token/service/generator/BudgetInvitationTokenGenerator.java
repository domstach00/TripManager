package com.example.tripmanager.shared.token.service.generator;

import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.token.BudgetInvitationToken;
import com.example.tripmanager.shared.token.model.token.Token;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Token generator for budget invitation tokens.
 */
@Component
public class BudgetInvitationTokenGenerator extends AbstractTokenGenerator<BudgetInvitationToken> {
    private static final Logger log = LoggerFactory.getLogger(BudgetInvitationTokenGenerator.class);

    /**
     * Generates a BudgetInvitationToken.
     *
     * @param accountId      ID of the account generating the token
     * @param additionalData Must contain a field with key {@link BudgetInvitationToken#FIELD_NAME_BUDGET_ID}
     * @return the generated BudgetInvitationToken
     * @throws IllegalArgumentException if budget ID is missing or invalid
     */
    @Override
    public BudgetInvitationToken generate(String accountId, Map<String, Object> additionalData) {
        BudgetInvitationToken budgetInvitationToken = generateTokenWithBasicValues(accountId);
        String budgetId = getSafeString(additionalData, BudgetInvitationToken.FIELD_NAME_BUDGET_ID);

        if (StringUtils.isBlank(budgetId)) {
            log.warn("Missing or empty budgetId in additionalData while generating BudgetInvitationToken for accountId={}", accountId);
            throw new IllegalArgumentException("Missing required field: " + BudgetInvitationToken.FIELD_NAME_BUDGET_ID);
        }

        budgetInvitationToken.setBudgetId(budgetId);
        return budgetInvitationToken;
    }

    @Override
    public TokenType getSupportedTokenType() {
        return TokenType.BUDGET_INVITATION;
    }

    @Override
    public Class<BudgetInvitationToken> getTokenImplementationClass() {
        return BudgetInvitationToken.class;
    }
}
