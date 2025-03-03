package com.example.tripmanager.shared.token.service;

import com.example.tripmanager.shared.token.config.TokenConfiguration;
import com.example.tripmanager.shared.token.model.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.repository.TokenRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;
    private final TokenConfiguration tokenConfiguration;

    @Autowired
    public TokenService(TokenRepository tokenRepository, TokenConfiguration tokenConfiguration) {
        this.tokenRepository = tokenRepository;
        this.tokenConfiguration = tokenConfiguration;
    }

    @PostConstruct
    public void init() {
        TokenType.applyConfiguration(tokenConfiguration);
    }

    public Token generateToken(@NotBlank String accountId, @NotNull TokenType tokenType) {
        log.info("Generating token (type={}) for accountId={}", tokenType.name(), accountId);
        // TODO: Apply verification information to the generated token
        final String tokenValue = UUID.randomUUID().toString();
        final LocalDateTime expirationDate = tokenType.calculateExpiration();
        final Token token = new Token(tokenValue, accountId, expirationDate, tokenType);
        return tokenRepository.save(token);
    }
}
