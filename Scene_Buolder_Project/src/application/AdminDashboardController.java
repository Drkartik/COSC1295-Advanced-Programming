package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDashboardController {

    @FXML
    private Button viewStockButton;
    
    @FXML
    private Button updateStockButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, String> titleColumn;
    
    @FXML
    private TableColumn<Book, String> authorsColumn;
    
    @FXML
    private TableColumn<Book, Integer> copiesColumn;
    
    @FXML
    private TableColumn<Book, Double> priceColumn;
    
    @FXML
    private TableColumn<Book, Integer> soldColumn;

    private String currentAdmin;

    public void setCurrentAdmin(String currentAdmin) {
        this.currentAdmin = currentAdmin;
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome, " + currentAdmin);
        }
    }

    @FXML
    public void initialize() {
        // Bind the columns to the Book properties
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorsColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        copiesColumn.setCellValueFactory(new PropertyValueFactory<>("physicalCopies"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        soldColumn.setCellValueFactory(new PropertyValueFactory<>("soldCopies"));
    }

    @FXML
    protected void handleViewStock() {
        String url = "jdbc:sqlite:test.db";
        String sql = "SELECT * FROM books";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            booksTable.getItems().clear();

            while (rs.next()) {
                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("authors"),
                        rs.getInt("physical_copies"),
                        rs.getDouble("price"),
                        rs.getInt("sold_copies")
                );

                booksTable.getItems().add(book);
            }

            booksTable.setVisible(true); // Show the books table
            showAlert(Alert.AlertType.INFORMATION, "View Stock", "Displaying stock of all books.");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load data from the database: " + e.getMessage());
        }
    }

    @FXML
    protected void handleUpdateStock() {
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a book to update.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Stock");
        dialog.setHeaderText("Update Stock for " + selectedBook.getTitle());
        dialog.setContentText("Enter stock change (positive to increase, negative to decrease):");

        dialog.showAndWait().ifPresent(input -> {
            try {
                int stockChange = Integer.parseInt(input);
                if (stockChange == 0) {
                    showAlert(Alert.AlertType.WARNING, "No Change", "Stock change cannot be zero.");
                    return; // Avoid updating the stock to zero
                }

                String url = "jdbc:sqlite:test.db";
                String sql = "UPDATE books SET physical_copies = physical_copies + ? WHERE title = ?";

                try (Connection conn = DriverManager.getConnection(url);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.setInt(1, stockChange);
                    pstmt.setString(2, selectedBook.getTitle());
                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        // Optionally fetch the updated book to display the new stock
                        showAlert(Alert.AlertType.INFORMATION, "Update Stock", "Stock updated successfully.");
                        handleViewStock(); // Refresh the stock view
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Update Stock", "Failed to update stock. Book may not exist.");
                    }

                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Database error: " + e.getMessage());
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
            }
        });
    }

    @FXML
    protected void handleLogout() {
        Stage currentStage = (Stage) logoutButton.getScene().getWindow();
        currentStage.close();
        
        // Load the login screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception (e.g., show an alert to the user)
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
