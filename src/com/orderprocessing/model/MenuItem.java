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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MenuItem)) return false;
        MenuItem menuItem = (MenuItem) o;
        return id != null && id.equalsIgnoreCase(menuItem.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.toLowerCase().hashCode() : 0;
    }

    public abstract void displayDetails();
}
