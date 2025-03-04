package com.example.tripmanager.shared.token.service;

import com.example.tripmanager.shared.token.config.TokenConfiguration;
import com.example.tripmanager.shared.token.exception.TokenNotFoundException;
import com.example.tripmanager.shared.token.exception.TokenValidationException;
import com.example.tripmanager.shared.token.model.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.tokenData.TokenData;
import com.example.tripmanager.shared.token.repository.TokenRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;
    private final TokenConfiguration tokenConfiguration;
    private final TokenDataStrategy tokenDataStrategy;

    @Autowired
    public TokenService(TokenRepository tokenRepository, TokenConfiguration tokenConfiguration, TokenDataStrategy tokenDataStrategy) {
        this.tokenRepository = tokenRepository;
        this.tokenConfiguration = tokenConfiguration;
        this.tokenDataStrategy = tokenDataStrategy;
    }

    @PostConstruct
    public void init() {
        TokenType.applyConfiguration(tokenConfiguration);
    }

    public Token generateToken(@NotBlank String accountId, @NotNull TokenType tokenType) {
        log.info("Generating token (type={}) for accountId={}", tokenType.name(), accountId);
        final LocalDateTime expirationDate = tokenType.calculateExpiration();
        TokenData tokenData = tokenDataStrategy.createTokenData()
                .accountId(accountId)
                .expirationDate(expirationDate)
                .tokenType(tokenType)
                .build();

        final String tokenValue = tokenDataStrategy.serializeAndSign(tokenData, tokenConfiguration.getSecret());
        Token token = new Token(tokenValue, accountId, expirationDate, tokenType);
        return tokenRepository.save(token);
    }

    public Optional<Token> validateAndGetToken(String tokenValue, TokenType expectedType) {
        try {
            TokenData tokenData = tokenDataStrategy.deserializeAndVerify(tokenValue, tokenConfiguration.getSecret());

            if (isTokenExpired(tokenData)) {
                log.warn("Token expired for account {}, token={}", tokenData.accountId(), tokenValue);
                return Optional.empty();
            }

            Token storedToken = tokenRepository.findTokenByTokenValue(tokenValue, tokenData.accountId())
                    .orElseThrow(() -> {
                        log.error("Token for this account ({}) was not found, token={}", tokenData.accountId(), tokenValue);
                        return new TokenNotFoundException("Token for this account was not found");
                    });
            boolean isTokenValid = tokenDataStrategy.validate(tokenData, storedToken, expectedType);
            if (!isTokenValid) {
                log.warn("Token is not valid, token={}", tokenValue);
                return Optional.empty();
            }
            return Optional.of(storedToken);
        } catch (TokenValidationException e) {
            log.error("Token validation exception, expectedType={} token={}", expectedType, tokenValue);
            return Optional.empty();
        }
    }

    // Method created to validate token expiration date before it is searched in database
    private boolean isTokenExpired(TokenData tokenData) {
        Clock clock = Clock.systemDefaultZone();
        return clock.millis() > tokenData.expirationMillis();
    }
}
