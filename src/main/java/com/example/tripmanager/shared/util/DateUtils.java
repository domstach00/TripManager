package com.example.tripmanager.shared.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateUtils {
    private DateUtils() {}

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));

    public static String formatInstantToDateString(Instant instant) {
        Objects.requireNonNull(instant, "Instant should not be null");
        return DATE_FORMATTER.format(instant);
    }
}
