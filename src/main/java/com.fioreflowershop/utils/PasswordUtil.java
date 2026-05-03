package com.fioreflowershop.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil - Utility class for password encryption and verification.
 * Uses BCrypt hashing algorithm for secure password management.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class PasswordUtil {

    // BCrypt work factor (cost) - higher = more secure, but slower
    private static final int BCRYPT_ROUNDS = 10;

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param plainPassword the plain-text password to hash
     * @return BCrypt hashed password string
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verifies a plain-text password against a BCrypt hash.
     *
     * @param plainPassword  the plain-text password to verify
     * @param hashedPassword the stored BCrypt hash
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}