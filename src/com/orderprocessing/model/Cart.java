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
            throw new EmptyCartException("Your shopping cart is currently empty.");
        }
        
        System.out.println("\n+----------------------------------------------------------------------------+");
        System.out.println("|                            YOUR SHOPPING CART                              |");
        System.out.println("+------+-----------------------------+----------+-------------+--------------+");
        System.out.printf("| %-4s | %-27s | %-8s | %-11s | %-12s |\n", "ID", "ITEM NAME", "QTY", "UNIT PRICE", "ITEM TOTAL");
        System.out.println("+------+-----------------------------+----------+-------------+--------------+");
        
        double subtotal = 0;
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            int qty = entry.getValue();
            double cost = item.getPrice() * qty;
            subtotal += cost;
            System.out.printf("| %-4s | %-27s | x%-7d | $%-10.2f | $%-11.2f |\n", 
                item.getId(), item.getName(), qty, item.getPrice(), cost);
        }
        
        System.out.println("+------+-----------------------------+----------+-------------+--------------+");
        System.out.printf("| %-59s | $%-11.2f |\n", "CART SUBTOTAL", subtotal);
        System.out.println("+-------------------------------------------------------------+--------------+");
    }

    public int getTotalItemCount() {
        int count = 0;
        for (int qty : items.values()) {
            count += qty;
        }
        return count;
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
