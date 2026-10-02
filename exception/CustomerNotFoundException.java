package com.orderprocessing.exception;

// Thrown when a requested customer ID does not exist.
public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
