package com.example.tripmanager.shared.token.service;

import com.example.tripmanager.shared.token.config.TokenConfiguration;
import com.example.tripmanager.shared.token.model.token.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.repository.TokenRepository;
import com.example.tripmanager.shared.token.service.factory.TokenGeneratorFactory;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Service responsible for generating and retrieving tokens of various types.
 * Utilizes {@link TokenGeneratorFactory} to create token instances and
 * {@link TokenRepository} to persist and fetch tokens from storage.
 */
@Service
public class TokenService {
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;
    private final TokenConfiguration tokenConfiguration;
    private final TokenGeneratorFactory tokenGeneratorFactory;

    /**
     * Constructor for injecting required dependencies.
     *
     * @param tokenRepository         repository for persisting and retrieving tokens
     * @param tokenConfiguration      configuration object for token behavior
     * @param tokenGeneratorFactory   factory used to generate tokens
     */
    @Autowired
    public TokenService(TokenRepository tokenRepository, TokenConfiguration tokenConfiguration, TokenGeneratorFactory tokenGeneratorFactory) {
        this.tokenRepository = tokenRepository;
        this.tokenConfiguration = tokenConfiguration;
        this.tokenGeneratorFactory = tokenGeneratorFactory;
    }

    /**
     * Initializes the token configuration after bean construction.
     * Automatically invoked by Spring after dependency injection is completed.
     */
    @PostConstruct
    public void init() {
        TokenType.applyConfiguration(tokenConfiguration);
    }

    /**
     * Generates a token for the specified account and token type with no additional data.
     *
     * @param accountId the ID of the user/account for which the token is generated
     * @param tokenType the type of token to generate
     * @return the generated and saved token
     */
    public <T extends Token> Token generateToken(@NotBlank String accountId, @NotNull TokenType tokenType) {
        return generateToken(accountId, tokenType, Map.of());
    }

    /**
     * Generates a token for the specified account and token type, using optional additional data.
     *
     * @param accountId      the ID of the user/account for which the token is generated
     * @param tokenType      the type of token to generate
     * @param additionalData a map of additional data used in token generation
     * @return the generated and saved token of the specified type
     */
    public <T extends Token> T generateToken(@NotBlank String accountId, @NotNull TokenType tokenType, @NotNull Map<String, Object> additionalData) {
        Class<T> tokenClass = tokenClassCast(tokenType.getAssociatedTokenClass());
        T token = tokenGeneratorFactory.generateToken(tokenType, accountId, additionalData, tokenClass);
        T savedToken = tokenRepository.save(token);
        log.info("Created Token (type={}) for accountId={}", savedToken.getTokenType().name(), savedToken.getAccountId());
        return savedToken;
    }

    /**
     * Retrieves a token by its value and expected type.
     *
     * @param tokenValue   the token value to search for
     * @param expectedType the expected type of the token
     * @return an Optional containing the token if found and castable to the expected type
     */
    public <T extends Token> Optional<T> getToken(@NotBlank String tokenValue, @NotNull TokenType expectedType) {
        Class<T> tokenClass = tokenClassCast(expectedType.getAssociatedTokenClass());
        Optional<Token> tokenOpt = tokenRepository.findTokenByTokenValue(tokenValue, expectedType);
        if (tokenOpt.isEmpty()) {
            log.warn("No token found for expectedType={} tokenValue={}", expectedType.name(), tokenValue);
        } else {
            log.info("Token found, expectedType={} tokenValue={}", expectedType.name(), tokenValue);
        }
        return tokenOpt.map(tokenClass::cast);
    }


    /**
     * Casts a generic Token class to a specific type.
     * Suppresses the unchecked warning due to the nature of type erasure.
     *
     * @param clazz the raw Token class to cast
     * @param <T>   the target token type
     * @return the class cast to the target type
     */
    @SuppressWarnings("unchecked")
    private static <T extends Token> Class<T> tokenClassCast(Class<? extends Token> clazz) {
        return (Class<T>) clazz;
    }
}
