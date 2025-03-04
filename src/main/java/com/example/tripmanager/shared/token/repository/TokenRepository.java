package com.example.tripmanager.shared.token.repository;

import com.example.tripmanager.shared.model.AbstractEntity;
import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import com.example.tripmanager.shared.token.model.Token;
import jakarta.validation.constraints.NotBlank;
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

    public Optional<Token> findTokenByTokenValue(@NotBlank String tokenValue, @NotBlank String accountId) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(
                Aggregation.match(
                        Criteria.where(Token.FIELD_NAME_TOKEN_VALUE).is(tokenValue)
                )
        );

        operationList.add(
                Aggregation.match(
                        Criteria.where(Token.FIELD_NAME_ACCOUNT_ID).is(AbstractEntity.toObjectId(accountId))
                )
        );

        return findOneBy(operationList);
    }
}
