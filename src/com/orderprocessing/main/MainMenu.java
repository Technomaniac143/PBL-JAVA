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
import java.util.Map;
import java.util.HashMap;

public class MainMenu {
    // ANSI Styling Constants
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private static final String BLUE = "\u001B[34m";

    private static List<MenuItem> menu = new ArrayList<>();
    private static Cart cart = new Cart();
    private static OrderDAO orderDAO = new OrderDAO();
    private static Scanner scanner = new Scanner(System.in);

    // Promo Code state
    private static String activePromoCode = null;
    private static double activeDiscountPercentage = 0.0;
    private static final Map<String, Double> PROMO_CODES = new HashMap<>();

    static {
        PROMO_CODES.put("WELCOME10", 10.0);
        PROMO_CODES.put("SAVER15", 15.0);
        PROMO_CODES.put("FOODIE20", 20.0);
    }

    public static void main(String[] args) {
        initializeMenu();
        printWelcomeHeader();

        boolean running = true;
        while (running) {
            printMainMenu();
            
            System.out.print(CYAN + BOLD + ">> Enter your choice (1-7): " + RESET);
            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) continue;

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                printError("Invalid choice! Please enter a number between 1 and 7.");
                continue;
            }

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
                    applyPromoCode();
                    break;
                case 7:
                    running = false;
                    printExitMessage();
                    break;
                default:
                    printError("Invalid selection. Please choose an option from 1 to 7.");
            }
        }
    }

    private static String stripAnsi(String str) {
        if (str == null) return "";
        return str.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    private static void printBoxRow(String content, int targetWidth, String frameColor) {
        String clean = stripAnsi(content);
        int visibleWidth = clean.length();
        int padding = Math.max(0, targetWidth - visibleWidth);
        StringBuilder sb = new StringBuilder();
        sb.append(frameColor).append(BOLD).append("| ").append(RESET);
        sb.append(content);
        for (int i = 0; i < padding; i++) {
            sb.append(' ');
        }
        sb.append(frameColor).append(BOLD).append(" |").append(RESET);
        System.out.println(sb.toString());
    }

    private static void printWelcomeHeader() {
        System.out.println(CYAN + BOLD + "================================================================================" + RESET);
        System.out.println(YELLOW + BOLD + "                    ONLINE ORDER PROCESSING SYSTEM" + RESET);
        System.out.println(PURPLE + "       PBL-JAVA Architecture - Real-Time Order Management System" + RESET);
        System.out.println(CYAN + BOLD + "================================================================================" + RESET);
    }

    private static void printMainMenu() {
        int totalItems = cart.getTotalItemCount();
        double subtotal = cart.getSubtotal();
        String cartBadge = totalItems == 0 ? " (Empty)" : String.format(" (%d %s - $%.2f)", totalItems, totalItems == 1 ? "item" : "items", subtotal);
        String promoBadge = activePromoCode == null ? "" : String.format(" [%s ACTIVE - %.0f%% OFF]", activePromoCode, activeDiscountPercentage);

        System.out.println("\n" + BLUE + BOLD + "+----------------------------------------------------------------------------+" + RESET);
        printBoxRow(BOLD + "                                 MAIN MENU" + RESET, 74, BLUE);
        System.out.println(BLUE + BOLD + "+----------------------------------------------------------------------------+" + RESET);
        printBoxRow(" [1] View Available Food Menu", 74, BLUE);
        printBoxRow(" [2] Add Item(s) to Shopping Cart", 74, BLUE);
        printBoxRow(" [3] Remove Item(s) from Shopping Cart", 74, BLUE);
        printBoxRow(" [4] View Shopping Cart" + GREEN + BOLD + cartBadge + RESET, 74, BLUE);
        printBoxRow(" [5] Checkout & Generate Receipt", 74, BLUE);
        printBoxRow(" [6] Apply Promo Code" + YELLOW + BOLD + promoBadge + RESET, 74, BLUE);
        printBoxRow(" [7] Exit System", 74, BLUE);
        System.out.println(BLUE + BOLD + "+----------------------------------------------------------------------------+" + RESET);
    }

    private static void initializeMenu() {
        menu.clear();
        menu.add(new FoodItem("F101", "Burger - Classic Beef", 8.99, "Mains"));
        menu.add(new FoodItem("F102", "Pizza - Margherita Large", 12.50, "Mains"));
        menu.add(new FoodItem("F103", "Pasta - Creamy Alfredo", 10.99, "Mains"));
        menu.add(new FoodItem("F104", "Salad - Fresh Caesar", 6.50, "Starters"));
        menu.add(new FoodItem("F105", "Beverage - Iced Lemon Tea", 2.50, "Drinks"));
        menu.add(new FoodItem("F106", "Beverage - Cold Brew Coffee", 3.75, "Drinks"));
        menu.add(new FoodItem("F107", "Dessert - Lava Cake", 4.99, "Desserts"));
    }

    private static void viewMenu() {
        System.out.println("\n" + CYAN + BOLD + "+----------------------------------------------------------------------------+" + RESET);
        printBoxRow(BOLD + "                           AVAILABLE FOOD MENU" + RESET, 74, CYAN);
        System.out.println(CYAN + BOLD + "+------+-----------------------------+--------------+------------------------+" + RESET);
        System.out.printf(CYAN + BOLD + "| %-4s | %-27s | %-12s | %-22s |\n" + RESET, "ID", "ITEM NAME", "CATEGORY", "PRICE");
        System.out.println(CYAN + BOLD + "+------+-----------------------------+--------------+------------------------+" + RESET);

        for (MenuItem item : menu) {
            FoodItem food = (FoodItem) item;
            System.out.printf("| " + BOLD + "%-4s" + RESET + " | %-27s | %-12s | " + GREEN + BOLD + "$%-20.2f" + RESET + " |\n",
                    food.getId(), food.getName(), food.getCategory(), food.getPrice());
        }

        System.out.println(CYAN + BOLD + "+------+-----------------------------+--------------+------------------------+" + RESET);
        System.out.print(YELLOW + "\n[Tip] Enter Item ID(s) to add (e.g. F101, F102 or F101:2, F102:3), or press Enter for Main Menu: " + RESET);
        
        if (!scanner.hasNextLine()) return;
        String quickInput = scanner.nextLine().trim();
        if (!quickInput.isEmpty()) {
            processAddItemsInput(quickInput);
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

    private static void processAddItemsInput(String input) {
        if (input == null || input.trim().isEmpty()) return;

        input = input.trim();

        // If input contains multiple items (commas, spaces, colons, multipliers)
        if (input.contains(",") || input.matches(".*\\s+.*") || input.contains(":") || input.contains("*") || input.matches(".*[xX=]\\d+.*")) {
            parseAndAddMultipleItems(input);
            return;
        }

        // Single Item ID entered
        MenuItem item = findMenuItem(input);
        if (item == null) {
            printError("Item with ID '" + input + "' was not found.");
            return;
        }

        promptAndAddToCart(item);
    }

    private static void parseAndAddMultipleItems(String input) {
        String[] tokens;
        if (input.contains(",")) {
            tokens = input.split(",");
        } else {
            tokens = input.split("\\s+");
        }

        List<String> addedDetails = new ArrayList<>();
        List<String> failedDetails = new ArrayList<>();

        for (String token : tokens) {
            token = token.trim();
            if (token.isEmpty()) continue;

            String id = token;
            int qty = 1;

            if (token.contains(":") || token.contains("*") || token.contains("x") || token.contains("X") || token.contains("=")) {
                String[] parts = token.split("[:*xX=]");
                if (parts.length >= 2) {
                    id = parts[0].trim();
                    try {
                        qty = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        qty = 1;
                    }
                }
            }

            MenuItem item = findMenuItem(id);
            if (item != null) {
                try {
                    cart.addItem(item, qty);
                    addedDetails.add(qty + "x [" + item.getName() + "]");
                } catch (InvalidQuantityException e) {
                    failedDetails.add(id + " (" + e.getMessage() + ")");
                }
            } else {
                failedDetails.add("'" + id + "'");
            }
        }

        if (!addedDetails.isEmpty()) {
            printSuccess("Added to cart: " + String.join(", ", addedDetails));
        }
        if (!failedDetails.isEmpty()) {
            printError("Could not find or add item(s): " + String.join(", ", failedDetails));
        }
    }

    private static void promptAndAddToCart(MenuItem item) {
        System.out.print(CYAN + "Enter quantity for " + BOLD + item.getName() + RESET + CYAN + " (default 1): " + RESET);
        if (!scanner.hasNextLine()) return;
        String qtyInput = scanner.nextLine().trim();
        int qty = 1;
        if (!qtyInput.isEmpty()) {
            try {
                qty = Integer.parseInt(qtyInput);
            } catch (NumberFormatException e) {
                printError("Invalid quantity. Added 1 item by default.");
                qty = 1;
            }
        }

        try {
            cart.addItem(item, qty);
            printSuccess("Added " + qty + "x [" + item.getName() + "] to your shopping cart!");
        } catch (InvalidQuantityException e) {
            printError(e.getMessage());
        }
    }

    private static void addItemToCart() {
        System.out.print(CYAN + "Enter Item ID(s) to add (e.g. F101 or F101, F102 or F101:2, F102:3): " + RESET);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            printError("Input cannot be empty.");
            return;
        }

        processAddItemsInput(input);
    }

    private static void removeItemFromCart() {
        if (cart.getItems().isEmpty()) {
            printError("Your shopping cart is currently empty.");
            return;
        }

        System.out.print(CYAN + "Enter Item ID(s) to remove (e.g. F101 or F101, F102): " + RESET);
        if (!scanner.hasNextLine()) return;
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;

        if (input.contains(",") || input.matches(".*\\s+.*")) {
            String[] tokens = input.contains(",") ? input.split(",") : input.split("\\s+");
            for (String tok : tokens) {
                tok = tok.trim();
                if (tok.isEmpty()) continue;
                MenuItem item = findMenuItem(tok);
                if (item != null) {
                    try {
                        cart.removeItem(item, 1);
                        printSuccess("Removed 1x [" + item.getName() + "] from cart.");
                    } catch (InvalidQuantityException e) {
                        printError(e.getMessage());
                    }
                } else {
                    printError("Item '" + tok + "' not found.");
                }
            }
            return;
        }

        MenuItem item = findMenuItem(input);
        if (item == null) {
            printError("Item with ID '" + input + "' not found in catalog.");
            return;
        }

        System.out.print(CYAN + "Enter quantity to remove (or type 'all'): " + RESET);
        if (!scanner.hasNextLine()) return;
        String qtyInput = scanner.nextLine().trim();
        
        int qtyToRemove;
        if (qtyInput.equalsIgnoreCase("all")) {
            qtyToRemove = cart.getItems().getOrDefault(item, 0);
        } else {
            try {
                qtyToRemove = Integer.parseInt(qtyInput);
            } catch (NumberFormatException e) {
                printError("Invalid quantity format.");
                return;
            }
        }

        try {
            cart.removeItem(item, qtyToRemove);
            printSuccess("Removed item from cart successfully.");
        } catch (InvalidQuantityException e) {
            printError(e.getMessage());
        }
    }

    private static void viewCart() {
        try {
            cart.displayCart();
            if (activePromoCode != null) {
                double subtotal = cart.getSubtotal();
                double discount = subtotal * (activeDiscountPercentage / 100.0);
                System.out.printf(YELLOW + "[Promo Code] '%s' Active: -%.0f%% (-$%.2f)\n" + RESET,
                        activePromoCode, activeDiscountPercentage, discount);
                System.out.printf(GREEN + BOLD + "Final Estimated Total: $%.2f\n" + RESET, subtotal - discount);
            }
        } catch (EmptyCartException e) {
            printInfo(e.getMessage());
        }
    }

    private static void applyPromoCode() {
        System.out.println("\n" + YELLOW + "[PROMO CODES AVAILABLE]:" + RESET);
        System.out.println("   * WELCOME10  (10% OFF)");
        System.out.println("   * SAVER15    (15% OFF)");
        System.out.println("   * FOODIE20   (20% OFF)");
        System.out.print(CYAN + "Enter Promo Code: " + RESET);
        if (!scanner.hasNextLine()) return;
        String code = scanner.nextLine().trim().toUpperCase();

        if (PROMO_CODES.containsKey(code)) {
            activePromoCode = code;
            activeDiscountPercentage = PROMO_CODES.get(code);
            printSuccess("Promo code '" + code + "' applied! You get " + activeDiscountPercentage + "% OFF your order.");
        } else {
            printError("Invalid or expired promo code. Please try WELCOME10, SAVER15, or FOODIE20.");
        }
    }

    private static void checkout() {
        try {
            if (cart.getItems().isEmpty()) {
                throw new EmptyCartException("Your shopping cart is empty.");
            }

            double rawSubtotal = cart.getSubtotal();
            double discountAmount = activePromoCode != null ? rawSubtotal * (activeDiscountPercentage / 100.0) : 0.0;
            double finalTotal = rawSubtotal - discountAmount;

            System.out.println("\n" + GREEN + BOLD + "================================================================================" + RESET);
            System.out.println(GREEN + BOLD + "                              OFFICIAL RECEIPT                                " + RESET);
            System.out.println(GREEN + BOLD + "================================================================================" + RESET);
            System.out.println(CYAN + "Timestamp: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + RESET);
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf(BOLD + "%-35s %-10s %-12s %-15s\n" + RESET, "ITEM NAME", "QTY", "UNIT PRICE", "TOTAL COST");
            System.out.println("--------------------------------------------------------------------------------");

            for (Map.Entry<MenuItem, Integer> entry : cart.getItems().entrySet()) {
                MenuItem item = entry.getKey();
                int qty = entry.getValue();
                double itemTotal = item.getPrice() * qty;
                System.out.printf("%-35s x%-9d $%-11.2f $%-14.2f\n", item.getName(), qty, item.getPrice(), itemTotal);
            }

            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-59s $%.2f\n", "SUBTOTAL:", rawSubtotal);

            if (activePromoCode != null) {
                System.out.printf(YELLOW + "%-59s -$%.2f (%.0f%% OFF)\n" + RESET, 
                        "DISCOUNT (" + activePromoCode + "):", discountAmount, activeDiscountPercentage);
            }

            System.out.printf("%-59s $%.2f\n", "TAX (0%):", 0.0);
            System.out.println("================================================================================");
            System.out.printf(GREEN + BOLD + "%-59s $%.2f\n" + RESET, "GRAND TOTAL PAID:", finalTotal);
            System.out.println("================================================================================");

            System.out.print(CYAN + BOLD + "\n>> Confirm placing this order? (Y/n): " + RESET);
            if (!scanner.hasNextLine()) return;
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("n")) {
                printInfo("Order checkout cancelled. Items remain in your cart.");
                return;
            }

            orderDAO.saveOrder(cart, finalTotal);
            printSuccess("Order successfully placed! Thank you for ordering.");

            cart.clear();
            activePromoCode = null;
            activeDiscountPercentage = 0.0;
        } catch (EmptyCartException e) {
            printError("Cannot checkout. " + e.getMessage());
        }
    }

    private static void printSuccess(String msg) {
        System.out.println("\n" + GREEN + BOLD + "[SUCCESS] " + msg + RESET);
    }

    private static void printError(String msg) {
        System.out.println("\n" + RED + BOLD + "[ERROR] " + msg + RESET);
    }

    private static void printInfo(String msg) {
        System.out.println("\n" + YELLOW + BOLD + "[INFO] " + msg + RESET);
    }

    private static void printExitMessage() {
        System.out.println("\n" + PURPLE + BOLD + "================================================================================" + RESET);
        System.out.println(YELLOW + BOLD + "      Thank you for using Online Order Processing System! Have a great day!  " + RESET);
        System.out.println(PURPLE + BOLD + "================================================================================" + RESET);
    }
}
