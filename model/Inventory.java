package com.orderprocessing.model;

// Represents stock information for a single product.
// Kept separate from Product so stock can change independently
// (and later be backed by its own DB table).
public class Inventory {
    private int productId;
    private int quantityAvailable;

    public Inventory(int productId, int quantityAvailable) {
        this.productId = productId;
        this.quantityAvailable = quantityAvailable;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId + " | Stock: " + quantityAvailable;
    }
}
