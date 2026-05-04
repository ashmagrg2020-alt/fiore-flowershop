package com.bloomcart.exception;

/**
 * Thrown when a requested resource (flower, order, user) does not exist.
 */
public class ResourceNotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
