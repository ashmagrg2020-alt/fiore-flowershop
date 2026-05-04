package com.bloomcart.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * Central validation utility.
 * All methods return null if valid, or an error message string if invalid.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[9][0-9]{9}$");           // Nepal 10-digit starting with 9

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-z\\s'.\\-]{2,100}$"); // letters, spaces, apostrophes, hyphens

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");

    private ValidationUtil() {}

    public static String validateFullName(String name) {
        if (name == null || name.isBlank()) return "Full name is required.";
        if (!NAME_PATTERN.matcher(name.trim()).matches())
            return "Full name must contain only letters and may include spaces, apostrophes or hyphens (2–100 characters).";
        return null;
    }

    public static String validateEmail(String email) {
        if (email == null || email.isBlank()) return "Email address is required.";
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) return "Please enter a valid email address.";
        return null;
    }

    public static String validatePhone(String phone) {
        if (phone == null || phone.isBlank()) return "Phone number is required.";
        String digits = phone.trim().replaceAll("[\\s\\-]", "");
        if (!PHONE_PATTERN.matcher(digits).matches())
            return "Phone must be a valid 10-digit Nepali number starting with 9.";
        return null;
    }

    public static String validatePassword(String password) {
        if (password == null || password.isBlank()) return "Password is required.";
        if (!PASSWORD_PATTERN.matcher(password).matches())
            return "Password must be at least 8 characters and include uppercase, lowercase, a digit, and a special character (@#$%^&+=!).";
        return null;
    }

    public static String validatePasswordConfirm(String password, String confirm) {
        if (confirm == null || confirm.isBlank()) return "Please confirm your password.";
        if (!password.equals(confirm)) return "Passwords do not match.";
        return null;
    }

    public static String validateFlowerName(String name) {
        if (name == null || name.isBlank()) return "Flower name is required.";
        if (name.trim().length() > 120) return "Flower name must not exceed 120 characters.";
        return null;
    }

    public static String validatePrice(String priceStr) {
        if (priceStr == null || priceStr.isBlank()) return "Price is required.";
        try {
            BigDecimal price = new BigDecimal(priceStr.trim());
            if (price.compareTo(BigDecimal.ZERO) <= 0) return "Price must be greater than zero.";
        } catch (NumberFormatException e) {
            return "Price must be a valid number.";
        }
        return null;
    }

    public static String validateStockQty(String qtyStr) {
        if (qtyStr == null || qtyStr.isBlank()) return "Stock quantity is required.";
        try {
            int qty = Integer.parseInt(qtyStr.trim());
            if (qty < 0) return "Stock quantity cannot be negative.";
        } catch (NumberFormatException e) {
            return "Stock quantity must be a valid integer.";
        }
        return null;
    }

    public static String validateOrderQuantity(int requested, int available) {
        if (requested <= 0) return "Order quantity must be at least 1.";
        if (requested > available) return "Only " + available + " item(s) available in stock.";
        return null;
    }

    public static String validateCategoryName(String name) {
        if (name == null || name.isBlank()) return "Category name is required.";
        if (name.trim().length() > 80) return "Category name must not exceed 80 characters.";
        return null;
    }

    public static String validateOccasionName(String name) {
        if (name == null || name.isBlank()) return "Occasion name is required.";
        if (name.trim().length() > 80) return "Occasion name must not exceed 80 characters.";
        return null;
    }

    public static String validateContactMessage(String name, String email, String message) {
        if (name == null || name.isBlank()) return "Name is required.";
        String emailErr = validateEmail(email);
        if (emailErr != null) return emailErr;
        if (message == null || message.isBlank()) return "Message cannot be empty.";
        return null;
    }

    /** Null-safe blank check for required text fields. */
    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
