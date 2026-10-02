package com.orderprocessing.exception;

// Thrown when there is a stock-related problem (e.g. not enough stock available).
public class InventoryException extends Exception {
    public InventoryException(String message) {
        super(message);
    }
}
