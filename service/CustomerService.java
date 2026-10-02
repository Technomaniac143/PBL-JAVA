package com.orderprocessing.service;

import com.orderprocessing.model.Customer;
import com.orderprocessing.exception.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Handles all customer-related operations.
// Currently stores data in memory using a HashMap (customerId -> Customer).
// Later, this class's internal storage can be swapped for JDBC/DB calls
// without changing how other classes use it.
public class CustomerService {
    private Map<Integer, Customer> customers = new HashMap<>();
    private int nextId = 1;

    // Adds a new customer and returns the generated customer ID
    public int addCustomer(String name, String email, String phone) {
        Customer customer = new Customer(nextId, name, email, phone);
        customers.put(nextId, customer);
        int assignedId = nextId;
        nextId++;
        return assignedId;
    }

    // Returns all customers as a list
    public List<Customer> viewCustomers() {
        return new ArrayList<>(customers.values());
    }

    // Fetches a single customer by ID, throws exception if not found
    public Customer getCustomerById(int customerId) throws CustomerNotFoundException {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
        return customer;
    }
}