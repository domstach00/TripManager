package com.example.tripmanager.shared.util;

import java.text.Normalizer;
import java.util.Locale;

public class SlugUtils {

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String withoutDiacritics = normalized.replaceAll("\\p{M}", "");

        String slug = withoutDiacritics
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-");

        return slug.replaceAll("^-|-$", "");
    }
}
