package com.orderprocessing.service;

import com.orderprocessing.model.Inventory;
import com.orderprocessing.exception.InventoryException;

import java.util.HashMap;
import java.util.Map;

// Handles stock levels for products.
// Stores data in memory using a HashMap (productId -> Inventory).
public class InventoryService {
    private Map<Integer, Inventory> stockMap = new HashMap<>();

    // Sets initial stock for a product (used for preloading sample data,
    // or when a new product is added)
    public void setStock(int productId, int quantity) {
        stockMap.put(productId, new Inventory(productId, quantity));
    }

    // Returns current stock available for a product (0 if not tracked yet)
    public int getStock(int productId) {
        Inventory inventory = stockMap.get(productId);
        return (inventory == null) ? 0 : inventory.getQuantityAvailable();
    }

    // Reduces stock after an order is placed.
    // Throws InventoryException if requested quantity is more than available.
    public void reduceStock(int productId, int quantityOrdered) throws InventoryException {
        int available = getStock(productId);
        if (quantityOrdered > available) {
            throw new InventoryException(
                    "Insufficient stock for product ID " + productId +
                    ". Available: " + available + ", Requested: " + quantityOrdered);
        }
        stockMap.get(productId).setQuantityAvailable(available - quantityOrdered);
    }
}
