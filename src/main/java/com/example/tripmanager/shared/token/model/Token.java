package com.example.tripmanager.shared.token.model;

import com.example.tripmanager.shared.model.AbstractEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = Token.COLLECTION_NAME)
public class Token extends AbstractEntity {
    public static final String COLLECTION_NAME = "tokens";

    @Indexed(unique = true)
    private String tokenValue;

    private ObjectId accountId;
    private LocalDateTime expirationDate;
    private boolean used;
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

    public ObjectId getAccountId() {
        return accountId;
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
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}
