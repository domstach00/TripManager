package com.example.tripmanager.shared.token.service.generator;

import com.example.tripmanager.shared.token.model.token.Token;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Base class for token generators providing common logic.
 *
 * @param <T> type of token
 */
public abstract class AbstractTokenGenerator<T extends Token> implements TokenGenerator<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractTokenGenerator.class);

    /**
     * Generates a random token value.
     *
     * @return random UUID string
     */
    protected String generateTokenValue() {
        return UUID.randomUUID().toString();
    }

    /**
     * Calculates the expiration date using the token type logic.
     *
     * @return expiration date/time
     */
    protected LocalDateTime generateExpirationDate() {
        return getSupportedTokenType().calculateExpiration();
    }

    /**
     * Creates a new token instance and populates it with base values.
     *
     * @param accountId ID of the account generating the token
     * @return token with base data set
     */
    protected T generateTokenWithBasicValues(String accountId) {
        try {
            Class<T> tokenClass = getTokenImplementationClass();
            if (tokenClass == null) {
                log.error("Token implementation class is null for token type {}", getSupportedTokenType());
                throw new IllegalStateException("Token implementation class must not be null");
            }

            T token = tokenClass.getDeclaredConstructor().newInstance();
            token.setTokenValue(generateTokenValue());
            token.setExpirationDate(generateExpirationDate());
            token.setAccountId(accountId);
            token.setTokenType(getSupportedTokenType());
            token.setUsed(false);

            return token;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            log.error("Failed to instantiate token (type={}, accountId={})", getSupportedTokenType(), accountId, e);
            throw new RuntimeException("Unable to instantiate token", e);
        }
    }

    /**
     * Retrieves a String value from a map safely, with logging.
     *
     * @param additionalData source map
     * @param fieldName      key to retrieve
     * @return value as string, or null if invalid
     */
    protected String getSafeString(Map<String, Object> additionalData, String fieldName) {
        if (additionalData == null || additionalData.isEmpty()) {
            log.warn("No additional data provided");
            return null;
        }

        if (StringUtils.isBlank(fieldName)) {
            log.warn("Field name is blank");
            return null;
        }

        Object value = additionalData.get(fieldName);
        if (value == null) {
            log.warn("No value found for key '{}'", fieldName);
            return null;
        }

        if (value instanceof String) {
            return (String) value;
        }

        log.warn("Value for key '{}' is not a String (found: {})", fieldName, value.getClass().getName());
        return null;
    }
}
