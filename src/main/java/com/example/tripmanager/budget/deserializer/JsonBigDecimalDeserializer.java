package com.example.tripmanager.budget.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class JsonBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    private static final int MAX_INTEGER_DIGITS = 10;
    private static final int MAX_FRACTION = 2;

    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getText();
        if (value == null || StringUtils.isBlank(value)) {
            return null;
        }
        BigDecimal result = new BigDecimal(value.trim());

        if (result.precision() - result.scale() > MAX_INTEGER_DIGITS) {
            throw new IOException("Number exceeds the maximum allowed integer digits (" + MAX_INTEGER_DIGITS + ").");
        }
        return result.setScale(MAX_FRACTION, RoundingMode.HALF_UP);
    }
}
