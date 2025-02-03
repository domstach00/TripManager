package com.example.tripmanager.shared.util;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    private DateUtils() {}

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("UTC"));

    public static String formatInstantToDateString(Instant instant) {
        if (instant == null) {
            return StringUtils.EMPTY;
        }
        return DATE_FORMATTER.format(instant);
    }
}
