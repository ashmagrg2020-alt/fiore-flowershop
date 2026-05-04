package com.bloomcart.exception;

/**
 * Thrown on authentication failures (wrong credentials, inactive account, etc.).
 */
public class AuthException extends AppException {

    private static final long serialVersionUID = 1L;

    public AuthException(String message) {
        super(message);
    }
}
