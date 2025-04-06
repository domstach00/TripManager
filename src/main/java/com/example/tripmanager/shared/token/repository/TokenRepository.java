package com.example.tripmanager.shared.token.repository;

import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.token.Token;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

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
}
