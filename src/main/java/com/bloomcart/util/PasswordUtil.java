package com.bloomcart.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Password hashing utility using SHA-256 + random salt.
 */
public class PasswordUtil {

    private static final int SALT_LENGTH = 16; // bytes → 32 hex chars

    private PasswordUtil() {}

    /**
     * Generates a cryptographically random hex salt.
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[SALT_LENGTH];
        random.nextBytes(saltBytes);
        return HexFormat.of().formatHex(saltBytes);
    }

    /**
     * Hashes (password + salt) with SHA-256, returns hex string.
     */
    public static String hash(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String combined = password + salt;
            byte[] hashBytes = md.digest(combined.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Verifies a plain-text password against a stored hash and salt.
     */
    public static boolean verify(String plainPassword, String storedHash, String salt) {
        String computed = hash(plainPassword, salt);
        return computed.equalsIgnoreCase(storedHash);
    }
}
