package com.example.tripmanager.shared.token.model;

import com.example.tripmanager.shared.model.AbstractEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = Token.COLLECTION_NAME)
public class Token extends AbstractEntity {
    public static final String COLLECTION_NAME = "tokens";
    public static final String FIELD_NAME_TOKEN_VALUE = "tokenValue";
    public static final String FIELD_NAME_ACCOUNT_ID = "accountId";
    public static final String FIELD_NAME_EXPIRATION_DATE = "expirationDate";
    public static final String FIELD_NAME_IS_USED = "isUsed";
    public static final String FIELD_NAME_TOKEN_TYPE = "tokenType";

    @Indexed(unique = true)
    private String tokenValue;

    private ObjectId accountId;
    private LocalDateTime expirationDate;
    private boolean isUsed;
    private TokenType tokenType;

    public Token() {
    }

    public Token(String tokenValue, String accountId, LocalDateTime expirationDate, TokenType tokenType) {
        this.tokenValue = tokenValue;
        this.accountId = toObjectId(accountId);
        this.expirationDate = expirationDate;
        this.tokenType = tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public String getAccountId() {
        return toString(accountId);
    }

    public void setAccountId(ObjectId accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        this.isUsed = used;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}
