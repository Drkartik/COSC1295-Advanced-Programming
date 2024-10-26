package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.File;



public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Book> topBooksTable;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, Integer> copiesColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TableColumn<Book, Integer> soldCopiesColumn;

    private List<Book> books = new ArrayList<>();
    private ShoppingCart shoppingCart;
    private String currentUsername;

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        welcomeLabel.setText("Welcome, " + username);
        shoppingCart = new ShoppingCart();
    }

    public DashboardController() {
        // Example book data
        books.add(new Book("Absolute Java", "Savitch", 10, 50.0, 142));
        books.add(new Book("JAVA: How to Program", "Deitel and Deitel", 100, 89.0, 475));
        books.add(new Book("Computing Concepts with JAVA 8 Essentials", "Horstman", 70, 99.0, 60));
        books.add(new Book("Java Software Solutions", "Lewis and Loftus", 100, 500.0, 12));
        books.add(new Book("Java Program Design", "Cohoon and Davidson", 2, 29.0, 86));
        books.add(new Book("Clean Code", "Robert Martin", 4, 68.0, 300));
        books.add(new Book("Gray Hat C#", "Brandon Perry", 100, 178.0, 79));
        books.add(new Book("Python Basics", "David Amos", 300, 1000.0, 42));
        books.add(new Book("Bayesian Statistics The Fun Way", "Will Kurt", 600, 45.0, 155));
    }

    @FXML
    public void initialize() {
        // Set up columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        soldCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));

        // Load top books
        loadTopBooks();
    }

    private void loadTopBooks() {
        topBooksTable.getItems().clear();

        // Get the top 5 books sorted by sold copies
        List<Book> topBooks = books.stream()
                .sorted(Comparator.comparingInt(Book::getSoldCopies).reversed())
                .limit(5)
                .toList();

        // Add the top books to the TableView
        topBooksTable.getItems().addAll(topBooks);
    }

    @FXML
    private void handleAddToCart() {
        Book selectedBook = topBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to add to the cart.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add to Cart");
        dialog.setHeaderText("Add " + selectedBook.getTitle() + " to your shopping cart.");
        dialog.setContentText("Enter quantity:");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int quantity = Integer.parseInt(input);
                if (quantity <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Invalid Quantity", "Please enter a positive quantity.");
                    return;
                }
                if (quantity > selectedBook.getPhysicalCopies()) {
                    showAlert(Alert.AlertType.WARNING, "Insufficient Stock", "Not enough copies available.");
                    return;
                }

                shoppingCart.addItem(selectedBook, quantity);
                showAlert(Alert.AlertType.INFORMATION, "Added to Cart", quantity + " copies of " + selectedBook.getTitle() + " added to the cart.");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
            }
        });
    }

    @FXML
    private void handleCheckout() {
        double totalPrice = shoppingCart.getTotalPrice();
        showAlert(Alert.AlertType.INFORMATION, "Total Price", "Your total is: $" + totalPrice);

        // Mock credit card information collection
        TextInputDialog cardDialog = new TextInputDialog();
        cardDialog.setTitle("Credit Card Information");
        cardDialog.setHeaderText("Enter your credit card details.");
        cardDialog.setContentText("Card Number (16 digits):");

        cardDialog.showAndWait().ifPresent(cardNumber -> {
            if (cardNumber.length() != 16 || !cardNumber.chars().allMatch(Character::isDigit)) {
                showAlert(Alert.AlertType.ERROR, "Invalid Card Number", "Card number must be 16 digits.");
                return;
            }

            // Expiry Date
            TextInputDialog expiryDialog = new TextInputDialog();
            expiryDialog.setTitle("Credit Card Expiry");
            expiryDialog.setHeaderText("Enter your card expiry date (MM/YYYY):");
            expiryDialog.setContentText("Expiry Date:");

            expiryDialog.showAndWait().ifPresent(expiry -> {
                String[] parts = expiry.split("/");
                if (parts.length != 2 || parts[0].length() != 2 || parts[1].length() != 4) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Expiry Date", "Enter in format MM/YYYY.");
                    return;
                }

                int month = Integer.parseInt(parts[0]);
                int year = Integer.parseInt(parts[1]);

                if (month < 1 || month > 12 || LocalDateTime.now().getYear() > year) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Expiry Date", "Expiry date must be in the future.");
                    return;
                }

                // CVV
                TextInputDialog cvvDialog = new TextInputDialog();
                cvvDialog.setTitle("Credit Card CVV");
                cvvDialog.setHeaderText("Enter your card CVV (3 digits):");
                cvvDialog.setContentText("CVV:");

                cvvDialog.showAndWait().ifPresent(cvv -> {
                    if (cvv.length() != 3 || !cvv.chars().allMatch(Character::isDigit)) {
                        showAlert(Alert.AlertType.ERROR, "Invalid CVV", "CVV must be 3 digits.");
                        return;
                    }

                    // Simulate payment success
                    String orderNumber = "ORD" + System.currentTimeMillis();
                    shoppingCart.placeOrder(orderNumber, currentUsername); // Pass customerName here
                    showAlert(Alert.AlertType.INFORMATION, "Order Placed", "Your order has been placed successfully! Order Number: " + orderNumber);
                    shoppingCart.clear();
                });
            });
        });
    }


    @FXML
    private void handleViewOrders() {
        // Display orders in a new window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view_orders.fxml"));
            Parent root = loader.load();
            ViewOrdersController viewOrdersController = loader.getController();
            viewOrdersController.setOrders(shoppingCart.getOrders());
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("View Orders");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportOrders() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Orders to File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(topBooksTable.getScene().getWindow());

            if (file != null) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (Order order : shoppingCart.getOrders()) {
                        writer.write("Order Number: " + order.getOrderNumber());
                        writer.newLine();
                        writer.write("Total Price: $" + order.getTotalPrice());
                        writer.newLine();
                        writer.write("Books:");
                        writer.newLine();
                        for (String bookDetails : order.getBookDetails()) {
                            writer.write(bookDetails);
                            writer.newLine();
                        }
                        writer.write("Date: " + order.getOrderDate());
                        writer.newLine();
                        writer.newLine();
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Orders exported successfully!");
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Failed to export orders: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogoutButtonAction() {
        // Close the current stage
        Stage stage = (Stage) topBooksTable.getScene().getWindow();
        stage.close();

        // Load the login screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditProfileButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_profile.fxml"));
            Parent root = loader.load();
            EditProfileController editProfileController = loader.getController();
            editProfileController.setCurrentUsername(currentUsername);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Profile");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
