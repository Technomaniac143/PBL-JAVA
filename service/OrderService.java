package com.orderprocessing.service;

import com.orderprocessing.model.Customer;
import com.orderprocessing.model.Order;
import com.orderprocessing.model.OrderItem;
import com.orderprocessing.model.Product;
import com.orderprocessing.exception.InventoryException;
import com.orderprocessing.exception.ProductNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Handles order creation and management.
// Depends on ProductService (to look up products) and
// InventoryService (to reduce stock after ordering).
public class OrderService {
    private Map<Integer, Order> orders = new HashMap<>();
    private int nextOrderId = 1;

    private ProductService productService;
    private InventoryService inventoryService;

    public OrderService(ProductService productService, InventoryService inventoryService) {
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    // Creates a new empty order for a customer and returns the order ID
    public int createOrder(Customer customer) {
        Order order = new Order(nextOrderId, customer);
        orders.put(nextOrderId, order);
        int assignedId = nextOrderId;
        nextOrderId++;
        return assignedId;
    }

    // Adds a product to an existing order and updates inventory accordingly
    public void addProductToOrder(int orderId, int productId, int quantity)
            throws ProductNotFoundException, InventoryException {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order with ID " + orderId + " not found.");
        }

        // Look up the product (throws ProductNotFoundException if missing)
        Product product = productService.getProductById(productId);

        // Reduce stock first (throws InventoryException if not enough stock)
        inventoryService.reduceStock(productId, quantity);

        // Add the item to the order
        order.addItem(new OrderItem(product, quantity));
    }

    // Returns the total price for a given order
    public double calculateOrderTotal(int orderId) {
        Order order = orders.get(orderId);
        return (order == null) ? 0 : order.calculateTotal();
    }

    // Displays full order details to console
    public void displayOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) {
            System.out.println("Order with ID " + orderId + " not found.");
            return;
        }
        order.displayOrder();
    }

    // Returns all orders (useful for future features/listing)
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
}
