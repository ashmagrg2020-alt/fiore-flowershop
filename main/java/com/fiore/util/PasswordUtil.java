package com.fiore.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil - Wraps BCrypt hashing so callers never deal with the library directly.
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Hashes a plain-text password using BCrypt with work-factor 12.
     *
     * @param plainText the raw password entered by the user
     * @return the BCrypt hash to store in the database
     */
    public static String hash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt(12));
    }

    /**
     * Verifies a plain-text password against a stored BCrypt hash.
     *
     * @param plainText the password submitted at login
     * @param hashed    the stored hash from the database
     * @return true if the password is correct
     */
    public static boolean verify(String plainText, String hashed) {
        if (plainText == null || hashed == null) return false;
        return BCrypt.checkpw(plainText, hashed);
    }
}
