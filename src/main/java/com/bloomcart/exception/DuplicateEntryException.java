package com.bloomcart.exception;

/**
 * Thrown when a unique constraint would be violated (email, phone, etc.).
 */
public class DuplicateEntryException extends AppException {

    private static final long serialVersionUID = 1L;

    public DuplicateEntryException(String message) {
        super(message);
    }
}
