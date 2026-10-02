package com.orderprocessing.model;

// Represents a Customer in the system.
// Kept simple (POJO) so it can later map easily to a DB table.
public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;

    public Customer(int customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId +
                " | Name: " + name +
                " | Email: " + email +
                " | Phone: " + phone;
    }
}
