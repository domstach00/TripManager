package com.example.tripmanager.shared.token.model.token;

import com.example.tripmanager.shared.token.model.TokenType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = Token.COLLECTION_NAME)
public class BudgetInvitationToken extends Token {

    public static final String FIELD_NAME_BUDGET_ID = "budgetId";
    private ObjectId budgetId;

    public BudgetInvitationToken() {
        super();
    }

    public BudgetInvitationToken(String tokenValue, String accountId, LocalDateTime expirationDate, TokenType tokenType, ObjectId budgetId) {
        super(tokenValue, accountId, expirationDate, tokenType);
        this.budgetId = budgetId;
    }

    public String getBudgetId() {
        return toString(budgetId);
    }

    public void setBudgetId(String  budgetId) {
        this.budgetId = toObjectId(budgetId);
    }
}
