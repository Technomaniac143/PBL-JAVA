package com.orderprocessing.service;

import com.orderprocessing.model.Product;
import com.orderprocessing.exception.ProductNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Handles all product-related operations.
// Stores data in memory using a HashMap (productId -> Product).
public class ProductService {
    private Map<Integer, Product> products = new HashMap<>();
    private int nextId = 1;

    // Adds a new product and returns the generated product ID
    public int addProduct(String name, double price) {
        Product product = new Product(nextId, name, price);
        products.put(nextId, product);
        int assignedId = nextId;
        nextId++;
        return assignedId;
    }

    // Returns all products as a list
    public List<Product> viewProducts() {
        return new ArrayList<>(products.values());
    }

    // Fetches a single product by ID, throws exception if not found
    public Product getProductById(int productId) throws ProductNotFoundException {
        Product product = products.get(productId);
        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }
        return product;
    }
}
