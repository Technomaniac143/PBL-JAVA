package com.orderprocessing.model;

// Represents a Product available for ordering.
// Stock/quantity is handled separately by Inventory, not here.
public class Product {
    private int productId;
    private String name;
    private double price;

    public Product(int productId, String name, double price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId +
                " | Name: " + name +
                " | Price: Rs. " + price;
    }
}
