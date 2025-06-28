package com.example.tripmanager.shared.util;

public class ColorUtils {
    public static final String HEX_COLOR_REGEX = "^#(?:[0-9a-fA-F]{3}){1,2}$";
    public static final String HEX_COLOR_ERROR_MESSAGE = "Color must be a valid hex code";

    /**
     * Checks if the given string is a valid hexadecimal color.
     * Supports 3-digit and 6-digit HEX formats (e.g., #FFF, #FFFFFF).
     *
     * @param color the string to validate
     * @return true if the string is a valid HEX color, false otherwise
     */
    public static boolean isValidHexColor(String color) {
        return color != null && color.matches(HEX_COLOR_REGEX);
    }

    /**
     * Normalizes the HEX color string to uppercase.
     * This method does not perform validation.
     *
     * @param color the HEX color string
     * @return the color in uppercase, or null if input is null
     */
    public static String normalizeHexColor(String color) {
        return color != null ? color.toUpperCase() : null;
    }
}
