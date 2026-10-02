package com.orderprocessing.exception;

// Thrown when a requested product ID does not exist.
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
