package com.bloomcart.exception;

/**
 * Thrown when an order quantity exceeds available stock.
 */
public class InsufficientStockException extends AppException {

    private static final long serialVersionUID = 1L;

    public InsufficientStockException(String message) {
        super(message);
    }
}
