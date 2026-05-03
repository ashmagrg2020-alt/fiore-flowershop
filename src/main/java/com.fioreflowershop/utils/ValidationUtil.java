package com.fioreflowershop.util;

import java.util.regex.Pattern;

/**
 * ValidationUtil - Utility class providing validation methods for all input fields.
 * Ensures data integrity before processing or storing user input.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class ValidationUtil {

    // Regex patterns for validation
    private static final Pattern EMAIL_PATTERN    = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN    = Pattern.compile("^[0-9]{10}$");
    private static final Pattern NAME_PATTERN     = Pattern.compile("^[A-Za-z\\s'-]{2,100}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private static final Pattern SKU_PATTERN      = Pattern.compile("^SKU-[0-9]{3,6}$");
    private static final Pattern PRICE_PATTERN    = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    /**
     * Checks if a string is null or empty after trimming.
     *
     * @param value the string to check
     * @return true if the string is null or empty
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates an email address format.
     *
     * @param email the email address to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates a phone number (10 digits).
     *
     * @param phone the phone number to validate
     * @return true if valid phone format
     */
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validates a full name (letters, spaces, hyphens, apostrophes only).
     * Prevents numeric input in name fields.
     *
     * @param name the full name to validate
     * @return true if valid name format
     */
    public static boolean isValidName(String name) {
        if (isNullOrEmpty(name)) return false;
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates password strength:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     *
     * @param password the password to validate
     * @return true if password meets requirements
     */
    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) return false;
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validates that two passwords match.
     *
     * @param password        the original password
     * @param confirmPassword the confirmation password
     * @return true if both passwords match
     */
    public static boolean passwordsMatch(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }

    /**
     * Validates a product SKU format (e.g., SKU-001).
     *
     * @param sku the SKU to validate
     * @return true if valid SKU format
     */
    public static boolean isValidSKU(String sku) {
        if (isNullOrEmpty(sku)) return false;
        return SKU_PATTERN.matcher(sku.trim()).matches();
    }

    /**
     * Validates a price value (positive decimal with up to 2 decimal places).
     *
     * @param price the price string to validate
     * @return true if valid price format
     */
    public static boolean isValidPrice(String price) {
        if (isNullOrEmpty(price)) return false;
        return PRICE_PATTERN.matcher(price.trim()).matches() && Double.parseDouble(price) > 0;
    }

    /**
     * Validates that a quantity is a positive integer.
     *
     * @param qty the quantity string to validate
     * @return true if valid quantity
     */
    public static boolean isValidQuantity(String qty) {
        if (isNullOrEmpty(qty)) return false;
        try {
            return Integer.parseInt(qty.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Sanitizes a string by trimming and removing HTML tags to prevent XSS.
     *
     * @param input the raw input string
     * @return sanitized string
     */
    public static String sanitize(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("<[^>]*>", "");
    }

    /**
     * Returns a human-readable error message for name validation.
     *
     * @return error message string
     */
    public static String getNameErrorMessage() {
        return "Full name must contain only letters, spaces, hyphens, or apostrophes (2–100 characters).";
    }

    /**
     * Returns a human-readable error message for password validation.
     *
     * @return error message string
     */
    public static String getPasswordErrorMessage() {
        return "Password must be at least 8 characters and include uppercase, lowercase, a number, and a special character.";
    }
}