package com.orderprocessing.main;

import com.orderprocessing.model.Cart;
import com.orderprocessing.model.FoodItem;
import com.orderprocessing.model.MenuItem;
import com.orderprocessing.dao.OrderDAO;
import com.orderprocessing.exception.EmptyCartException;
import com.orderprocessing.exception.InvalidQuantityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu {
    private static List<MenuItem> menu = new ArrayList<>();
    private static Cart cart = new Cart();
    private static OrderDAO orderDAO = new OrderDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeMenu();
        
        boolean running = true;
        while (running) {
            System.out.println("\n=====================================================");
            System.out.println("          ONLINE ORDER PROCESSING SYSTEM");
            System.out.println("=====================================================");
            System.out.println("1. View Food Menu");
            System.out.println("2. Add Item to Cart");
            System.out.println("3. Remove Item from Cart");
            System.out.println("4. View Shopping Cart");
            System.out.println("5. Checkout and Generate Bill");
            System.out.println("6. Exit");
            System.out.println("-----------------------------------------------------");
            System.out.print("Enter your choice: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
                continue;
            }
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewMenu();
                    break;
                case 2:
                    addItemToCart();
                    break;
                case 3:
                    removeItemFromCart();
                    break;
                case 4:
                    viewCart();
                    break;
                case 5:
                    checkout();
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting system. Have a great day!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }
        }
    }

    private static void initializeMenu() {
        menu.add(new FoodItem("F101", "Burger - Classic Beef", 8.99, "Main"));
        menu.add(new FoodItem("F102", "Pizza - Margherita Large", 12.50, "Main"));
        menu.add(new FoodItem("F103", "Beverage - Iced Tea", 2.50, "Drinks"));
    }

    private static void viewMenu() {
        System.out.println("\n--- AVAILABLE MENU ---");
        for (MenuItem item : menu) {
            System.out.printf("[%s] %s : $%.2f\n", item.getId(), item.getName(), item.getPrice());
        }
    }

    private static MenuItem findMenuItem(String id) {
        for (MenuItem item : menu) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }
        return null;
    }

    private static void addItemToCart() {
        System.out.print("Enter Item ID to add: ");
        String id = scanner.nextLine();
        MenuItem item = findMenuItem(id);
        
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.print("Enter quantity: ");
        if (scanner.hasNextInt()) {
            int qty = scanner.nextInt();
            scanner.nextLine();
            try {
                cart.addItem(item, qty);
                System.out.println("Item added to cart successfully.");
            } catch (InvalidQuantityException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid quantity.");
            scanner.nextLine();
        }
    }

    private static void removeItemFromCart() {
        System.out.print("Enter Item ID to remove: ");
        String id = scanner.nextLine();
        MenuItem item = findMenuItem(id);
        
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        
        System.out.print("Enter quantity to remove: ");
        if (scanner.hasNextInt()) {
            int qty = scanner.nextInt();
            scanner.nextLine();
            try {
                cart.removeItem(item, qty);
                System.out.println("Item removed successfully.");
            } catch (InvalidQuantityException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid quantity.");
            scanner.nextLine();
        }
    }

    private static void viewCart() {
        try {
            cart.displayCart();
        } catch (EmptyCartException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void checkout() {
        try {
            cart.displayCart();
            double total = cart.getSubtotal();
            System.out.println("-----------------------------------------------------");
            System.out.printf("TOTAL BILL: $%.2f\n", total);
            
            orderDAO.saveOrder(cart, total);
            System.out.println("Order processed and saved to database successfully!");
            cart.clear();
        } catch (EmptyCartException e) {
            System.out.println("Cannot checkout. " + e.getMessage());
        }
    }
}
