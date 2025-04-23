package com.example.tripmanager.shared.token.repository;

import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.token.Token;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepository extends AbstractRepositoryImpl<Token> {
    @Override
    protected Class<Token> getEntityClass() {
        return Token.class;
    }

    public Optional<Token> findTokenByTokenValue(@NotBlank String tokenValue, @NotNull TokenType tokenType) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        Criteria.where(Token.FIELD_NAME_TOKEN_VALUE).is(tokenValue)
                )
        );

        operationList.add(
                Aggregation.match(
                        Criteria.where(Token.FIELD_NAME_TOKEN_TYPE).is(tokenType.name())
                )
        );

        return findOneBy(operationList);
    }

    public Optional<Token> findValidTokenWithLatestExpirationForAccount(@NotBlank String accountId, LocalDateTime timeNow) {
        List<AggregationOperation> operationList = new ArrayList<>();

        operationList.add(
                Aggregation.match(
                        Criteria.where(Token.FIELD_NAME_ACCOUNT_ID).is(AbstractEntity.toObjectId(accountId))
                )
        );

        operationList.add(
                Aggregation.match(
                        Criteria.where(Token.FIELD_NAME_EXPIRATION_DATE).gt(timeNow)
                )
        );

        operationList.add(
                Aggregation.match(
                        Criteria.where(Token.FIELD_NAME_IS_USED).is(false)
                )
        );

        operationList.add(
                Aggregation.sort(Sort.Direction.DESC, Token.FIELD_NAME_EXPIRATION_DATE)
        );

        operationList.add(
                Aggregation.limit(1)
        );

        return findOneBy(operationList);
    }
}
