package com.orderprocessing.model;

import com.orderprocessing.exception.InvalidQuantityException;
import com.orderprocessing.exception.EmptyCartException;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<MenuItem, Integer> items;

    public Cart() {
        this.items = new HashMap<>();
    }

    public void addItem(MenuItem item, int quantity) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(MenuItem item, int quantity) throws InvalidQuantityException {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }
        if (items.containsKey(item)) {
            int currentQty = items.get(item);
            if (quantity >= currentQty) {
                items.remove(item);
            } else {
                items.put(item, currentQty - quantity);
            }
        }
    }

    public void displayCart() throws EmptyCartException {
        if (items.isEmpty()) {
            throw new EmptyCartException("Your shopping cart is empty.");
        }
        System.out.println("--- YOUR CART ---");
        double subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            double cost = item.getPrice() * qty;
            subtotal += cost;
            System.out.printf("%s x%d : $%.2f\n", item.getName(), qty, cost);
        }
        System.out.printf("Subtotal: $%.2f\n", subtotal);
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            subtotal += entry.getKey().getPrice() * entry.getValue();
        }
        return subtotal;
    }

    public void clear() {
        items.clear();
    }
    
    public Map<MenuItem, Integer> getItems() {
        return items;
    }
}
