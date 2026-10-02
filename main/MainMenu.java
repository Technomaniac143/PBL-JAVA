package com.orderprocessing.main;

import com.orderprocessing.model.Customer;
import com.orderprocessing.model.Product;
import com.orderprocessing.service.CustomerService;
import com.orderprocessing.service.InventoryService;
import com.orderprocessing.service.OrderService;
import com.orderprocessing.service.ProductService;
import com.orderprocessing.exception.CustomerNotFoundException;
import com.orderprocessing.exception.InventoryException;
import com.orderprocessing.exception.ProductNotFoundException;

import java.util.List;
import java.util.Scanner;

// Console-based menu that drives the whole application.
public class MainMenu {

    // Services are created once here and shared across menu options
    private static CustomerService customerService = new CustomerService();
    private static ProductService productService = new ProductService();
    private static InventoryService inventoryService = new InventoryService();
    private static OrderService orderService = new OrderService(productService, inventoryService);

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        preloadSampleData();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: addCustomer(); break;
                case 2: viewCustomers(); break;
                case 3: addProduct(); break;
                case 4: viewProducts(); break;
                case 5: createOrder(); break;
                case 6: addProductToOrder(); break;
                case 7: calculateOrderTotal(); break;
                case 8: displayOrder(); break;
                case 9:
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // Preloads a few sample customers and products for demo purposes
    private static void preloadSampleData() {
        customerService.addCustomer("Arjun Kumar", "arjun@example.com", "9876543210");
        customerService.addCustomer("Priya Singh", "priya@example.com", "9123456780");

        int p1 = productService.addProduct("Laptop", 55000.0);
        int p2 = productService.addProduct("Mouse", 500.0);
        int p3 = productService.addProduct("Keyboard", 1200.0);

        inventoryService.setStock(p1, 10);
        inventoryService.setStock(p2, 50);
        inventoryService.setStock(p3, 30);
    }

    private static void printMenu() {
        System.out.println("\n===== Order Processing System =====");
        System.out.println("1. Add Customer");
        System.out.println("2. View Customers");
        System.out.println("3. Add Product");
        System.out.println("4. View Products");
        System.out.println("5. Create Order");
        System.out.println("6. Add Product to Order");
        System.out.println("7. Calculate Order Total");
        System.out.println("8. Display Order Details");
        System.out.println("9. Exit");
    }

    private static void addCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        int id = customerService.addCustomer(name, email, phone);
        System.out.println("Customer added successfully with ID: " + id);
    }

    private static void viewCustomers() {
        List<Customer> customers = customerService.viewCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        for (Customer c : customers) {
            System.out.println(c);
        }
    }

    private static void addProduct() {
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        double price = readDouble("Enter price: ");
        int stock = readInt("Enter initial stock quantity: ");

        int id = productService.addProduct(name, price);
        inventoryService.setStock(id, stock);
        System.out.println("Product added successfully with ID: " + id);
    }

    private static void viewProducts() {
        List<Product> products = productService.viewProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product p : products) {
            System.out.println(p + " | Stock: " + inventoryService.getStock(p.getProductId()));
        }
    }

    private static void createOrder() {
        int customerId = readInt("Enter customer ID: ");
        try {
            Customer customer = customerService.getCustomerById(customerId);
            int orderId = orderService.createOrder(customer);
            System.out.println("Order created successfully with ID: " + orderId);
        } catch (CustomerNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addProductToOrder() {
        int orderId = readInt("Enter order ID: ");
        int productId = readInt("Enter product ID: ");
        int quantity = readInt("Enter quantity: ");

        try {
            orderService.addProductToOrder(orderId, productId, quantity);
            System.out.println("Product added to order successfully.");
        } catch (ProductNotFoundException | InventoryException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void calculateOrderTotal() {
        int orderId = readInt("Enter order ID: ");
        double total = orderService.calculateOrderTotal(orderId);
        System.out.println("Order Total: Rs. " + total);
    }

    private static void displayOrder() {
        int orderId = readInt("Enter order ID: ");
        orderService.displayOrder(orderId);
    }

    // Helper to safely read an integer from console
    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            System.out.print(prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume leftover newline
        return value;
    }

    // Helper to safely read a double from console
    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalid input. Please enter a valid price.");
            System.out.print(prompt);
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // consume leftover newline
        return value;
    }
}
