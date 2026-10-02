package com.orderprocessing.model;

// Represents a single line item inside an Order (a product + quantity ordered).
public class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    // Subtotal for this line item (price * quantity)
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x " + quantity +
                " = Rs. " + getSubtotal();
    }
}
