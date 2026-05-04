package com.bloomcart.exception;

/**
 * Thrown when user-supplied input fails validation rules.
 */
public class ValidationException extends AppException {

    private static final long serialVersionUID = 1L;

    public ValidationException(String message) {
        super(message);
    }
}
