package application;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private String orderNumber;
    private String customerName; // Added customerName property
    private LocalDateTime orderDate;
    private double totalPrice;
    private List<String> bookDetails;

    // Constructor with customerName included
    public Order(String orderNumber, String customerName, LocalDateTime orderDate, double totalPrice, List<String> bookDetails) {
        this.orderNumber = orderNumber;
        this.customerName = customerName; // Initialize customerName
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.bookDetails = bookDetails;
    }

    // Getters
    public String getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerName() {
        return customerName; // Getter for customerName
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<String> getBookDetails() {
        return bookDetails;
    }

    // Setters
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName; // Setter for customerName
    }

    public void setBookDetails(List<String> bookDetails) {
        this.bookDetails = bookDetails; // Setter for bookDetails
    }
}
