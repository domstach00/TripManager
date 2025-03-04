package com.example.tripmanager.shared.token.service;

import com.example.tripmanager.shared.token.config.TokenProperties;
import com.example.tripmanager.shared.token.exception.TokenValidationException;
import com.example.tripmanager.shared.token.model.Token;
import com.example.tripmanager.shared.token.model.TokenType;
import com.example.tripmanager.shared.token.model.tokenData.DefaultTokenDataBuilder;
import com.example.tripmanager.shared.token.model.tokenData.TokenData;
import com.example.tripmanager.shared.token.model.tokenData.TokenDataBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Primary
public class DefaultTokenDataStrategy implements TokenDataStrategy {
    private final TokenProperties tokenProperties;

    @Autowired
    public DefaultTokenDataStrategy(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public TokenDataBuilder createTokenData() {
        return new DefaultTokenDataBuilder();
    }

    @Override
    public String serializeAndSign(TokenData data, String secret) {
        try {
            String header = Base64.getUrlEncoder().encodeToString("alg=HS256".getBytes(StandardCharsets.UTF_8));
            String payload = Base64.getUrlEncoder().encodeToString(serializePayload(data).getBytes(StandardCharsets.UTF_8));
            String signature = calculateSignature(header + "." + payload, secret);
            return header + "." + payload + "." + signature;
        } catch (Exception e) {
            throw new TokenValidationException("Token serialization failed");
        }
    }

    private String serializePayload(TokenData data) {
        return String.join(tokenProperties.getSeparator(),
                data.accountId(),
                String.valueOf(data.expirationMillis()),
                data.tokenType().name(),
                data.additionalData().entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(","))
        );
    }

    @Override
    public TokenData deserializeAndVerify(String tokenValue, String secret) {
        String[] parts = tokenValue.split("\\.");
        if (parts.length != 3) {
            throw new TokenValidationException("Invalid token format");
        }

        verifySignature(parts[0] + "." + parts[1], parts[2], secret);
        return deserializePayload(new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8));
    }

    private TokenData deserializePayload(String payload) {
        String[] elements = payload.split(Pattern.quote(tokenProperties.getSeparator()));
        DefaultTokenDataBuilder builder = new DefaultTokenDataBuilder();

        // AccountId (element 0)
        if (elements.length >= 1 && !elements[0].isEmpty()) {
            builder.accountId(elements[0]);
        }

        // ExpirationDate (element 1)
        if (elements.length >= 2 && !elements[1].isEmpty()) {
            try {
                builder.expirationDate(Long.parseLong(elements[1]));
            } catch (NumberFormatException ignored) {
                // Skip
            }
        }

        // TokenType (element 2)
        if (elements.length >= 3 && !elements[2].isEmpty()) {
            try {
                builder.tokenType(TokenType.valueOf(elements[2]));
            } catch (IllegalArgumentException ignored) {
                // Skip
            }
        }

        // AdditionalData (element 3)
        Map<String, String> additionalData = Collections.emptyMap();
        if (elements.length >= 4 && !elements[3].isEmpty()) {
            additionalData = parseAdditionalData(elements[3]);
        }
        builder.addAdditionalData(additionalData);

        return builder.build();
    }

    private Map<String, String> parseAdditionalData(String data) {
        return Arrays.stream(data.split(","))
                .map(entry -> entry.split("="))
                .filter(pair -> pair.length == 2)
                .collect(Collectors.toMap(
                        pair -> pair[0],
                        pair -> pair[1],
                        (existing, replacement) -> existing
                ));
    }

    private String calculateSignature(String data, String secret) {
        try {
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new TokenValidationException("Signature calculation failed");
        }
    }

    private void verifySignature(String data, String receivedSignature, String secret) {
        String calculatedSignature = calculateSignature(data, secret);
        if (!calculatedSignature.equals(receivedSignature)) {
            throw new TokenValidationException("Signature verification failed");
        }
    }

    @Override
    public boolean validate(TokenData data, Token storedToken, TokenType expectedType) {
        Clock clock = Clock.systemDefaultZone();
        return data.expirationMillis() > clock.millis()
                && Objects.equals(storedToken.getTokenType(), expectedType)
                && Objects.equals(storedToken.getAccountId(), data.accountId());
    }
}
