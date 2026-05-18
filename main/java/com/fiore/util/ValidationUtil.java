package com.fiore.util;

/**
 * ValidationUtil - Reusable validation methods used across controllers and services.
 * All methods are static; the class should not be instantiated.
 */
public class ValidationUtil {

    private ValidationUtil() {}

    // ── String checks ──────────────────────────────────────────────────────

    /** Returns true if the value is null or contains only whitespace. */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // ── Name validation ────────────────────────────────────────────────────

    /**
     * A valid full name:
     *  - is not blank
     *  - contains only letters and spaces
     *  - is between 2 and 80 characters long
     */
    public static boolean isValidName(String name) {
        if (isNullOrEmpty(name)) return false;
        String trimmed = name.trim();
        return trimmed.matches("[A-Za-z ]{2,80}");
    }

    // ── Email validation ───────────────────────────────────────────────────

    /** Basic RFC-5321-style email check. */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return email.trim().matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$");
    }

    // ── Phone validation ───────────────────────────────────────────────────

    /**
     * Accepts international format with optional leading '+'.
     * 7–15 digits (spaces and dashes allowed).
     */
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return phone.trim().matches("^\\+?[\\d\\s\\-]{7,15}$");
    }

    // ── Password validation ────────────────────────────────────────────────

    /** Password must be at least 6 characters long. */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /** Returns true if the two password strings match. */
    public static boolean passwordsMatch(String pw1, String pw2) {
        return pw1 != null && pw1.equals(pw2);
    }

    // ── Price / quantity validation ────────────────────────────────────────

    /** Returns true if the string represents a positive number. */
    public static boolean isPositiveNumber(String value) {
        if (isNullOrEmpty(value)) return false;
        try {
            return Double.parseDouble(value.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /** Returns true if the string is a non-negative integer. */
    public static boolean isNonNegativeInt(String value) {
        if (isNullOrEmpty(value)) return false;
        try {
            return Integer.parseInt(value.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // ── General ───────────────────────────────────────────────────────────

    /**
     * Sanitises a string for safe HTML display (prevents XSS in JSP EL).
     * Replace any raw HTML characters with their entity equivalents.
     */
    public static String sanitise(String input) {
        if (input == null) return "";
        return input
            .replace("&",  "&amp;")
            .replace("<",  "&lt;")
            .replace(">",  "&gt;")
            .replace("\"", "&quot;")
            .replace("'",  "&#x27;");
    }
}
