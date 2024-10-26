package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private Map<Book, Integer> items;
    private List<Order> orders;

    public ShoppingCart() {
        items = new HashMap<>();
        orders = new ArrayList<>();
    }

    public void addItem(Book book, int quantity) {
        items.put(book, items.getOrDefault(book, 0) + quantity);
    }

    public void clear() {
        items.clear();
    }

    public double getTotalPrice() {
        return items.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
                .sum();
    }

    public void placeOrder(String orderNumber, String customerName) { // Added customerName parameter
        List<String> bookDetails = new ArrayList<>();
        double totalPrice = getTotalPrice();

        for (Map.Entry<Book, Integer> entry : items.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();
            bookDetails.add(book.getTitle() + " - Quantity: " + quantity);
            book.setPhysicalCopies(book.getPhysicalCopies() - quantity); // Decrease stock
        }

        // Create a new order with the customerName
        Order order = new Order(orderNumber, customerName, LocalDateTime.now(), totalPrice, bookDetails);
        orders.add(order);
        clear(); // Clear cart after placing the order
    }

    public List<Order> getOrders() {
        return orders;
    }
}
