package com.orderprocessing.model;

public abstract class MenuItem {
    protected String id;
    protected String name;
    protected double price;

    public MenuItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public abstract void displayDetails();
}
