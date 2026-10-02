package com.orderprocessing.model;

import java.util.ArrayList;
import java.util.List;

// Represents a Customer's Order, made up of one or more OrderItems.
public class Order {
    private int orderId;
    private Customer customer;
    private List<OrderItem> items;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    // Adds a product line item to this order
    public void addItem(OrderItem item) {
        items.add(item);
    }

    // Calculates total price of the whole order
    public double calculateTotal() {
        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    // Prints full order details to console
    public void displayOrder() {
        System.out.println("---- Order #" + orderId + " ----");
        System.out.println("Customer: " + customer.getName());
        System.out.println("Items:");
        for (OrderItem item : items) {
            System.out.println("  " + item);
        }
        System.out.println("Total: Rs. " + calculateTotal());
        System.out.println("--------------------------");
    }
}
