package com.orderprocessing.model;

public class FoodItem extends MenuItem implements Discountable {
    private String category;

    public FoodItem(String id, String name, double price, String category) {
        super(id, name, price);
        this.category = category;
    }

    @Override
    public void displayDetails() {
        System.out.printf("[%s] %s (%s) : $%.2f\n", id, name, category, price);
    }

    @Override
    public double applyDiscount(double percentage) {
        return price - (price * (percentage / 100.0));
    }
}
