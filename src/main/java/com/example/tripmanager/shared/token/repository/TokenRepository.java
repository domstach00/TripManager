package com.example.tripmanager.shared.token.repository;

import com.example.tripmanager.shared.repository.AbstractRepositoryImpl;
import com.example.tripmanager.shared.token.model.Token;
import org.springframework.stereotype.Repository;

@Repository
public class TokenRepository extends AbstractRepositoryImpl<Token> {
    @Override
    protected Class<Token> getEntityClass() {
        return Token.class;
    }
}
