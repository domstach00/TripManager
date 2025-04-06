package com.example.tripmanager.shared.token.service.factory;

import com.example.tripmanager.shared.token.model.token.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.service.generator.TokenGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Factory responsible for delegating token generation to the appropriate generator
 * based on the token type.
 */
@Component
public class TokenGeneratorFactory {
    private static final Logger log = LoggerFactory.getLogger(TokenGeneratorFactory.class);

    private final Map<TokenType, TokenGenerator<? extends Token>> generatorMap = new EnumMap<>(TokenType.class);

    /**
     * Initializes the factory with all available token generators.
     *
     * @param generators list of available TokenGenerator implementations
     */
    @Autowired
    public TokenGeneratorFactory(List<TokenGenerator<? extends Token>> generators) {
        for (TokenGenerator<? extends Token> generator : generators) {
            TokenType tokenType = generator.getSupportedTokenType();
            if (generatorMap.containsKey(tokenType)) {
                log.warn("Duplicate token generator for type: {}", tokenType);
            }
            generatorMap.put(tokenType, generator);
        }
    }

    /**
     * Generates a token of a given type for a specific account.
     *
     * @param tokenType     the type of token to generate
     * @param accountId     the ID of the account requesting the token
     * @param additionalData optional data required by the generator
     * @param tokenClass    expected class of the generated token
     * @param <T>           type of the expected token
     * @return generated token
     * @throws IllegalArgumentException if no generator supports the given type
     * @throws ClassCastException       if the generated token is not of the expected class
     */
    public <T extends Token> T generateToken(@NotNull TokenType tokenType, @NotBlank String accountId, Map<String, Object> additionalData, Class<T> tokenClass) {
        TokenGenerator<? extends Token> generator = generatorMap.get(tokenType);

        if (generator == null) {
            log.warn("Unsupported token type={} requested by={}", tokenType.name(), accountId);
            throw new IllegalArgumentException("Unsupported token type: " + tokenType.name() + " requested by: " + accountId);
        }

        Token token = generator.generate(accountId, additionalData);

        if (!tokenClass.isInstance(token)) {
            throw new ClassCastException("Generated token is not of expected type: " + tokenClass.getSimpleName());
        }

        return tokenClass.cast(token);
    }
}
