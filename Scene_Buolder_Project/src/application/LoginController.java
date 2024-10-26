package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text createAccountLink; // Add this line

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        String url = "jdbc:sqlite:test.db";
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        // Clear any previous alerts
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both username and password.");
            return; // Exit early
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String loggedInUsername = rs.getString("username"); // Retrieve username from ResultSet
                String role = rs.getString("role"); // Retrieve role from ResultSet

                System.out.println("Logged in username: " + loggedInUsername); // Debug line
                System.out.println("User role: " + role); // Debug line

                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + rs.getString("first_name") + "!");

                // Check user role and load appropriate dashboard
                if ("admin".equalsIgnoreCase(role)) {
                    loadAdminDashboard(loggedInUsername);  // Load Admin Dashboard
                } else {
                    loadDashboard(loggedInUsername);  // Load User Dashboard
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }


    @FXML
    protected void handleCreateAccountClick() {
        // Navigate to the sign-up page
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signup.fxml")); // Make sure this points to your signup FXML
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(root, 600, 400)); // Adjust size as needed
            stage.show();

            // Close the login window
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadDashboard(String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = fxmlLoader.load();

            // Set the current username in DashboardController
            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.setCurrentUsername(username);

            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close login window
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void loadAdminDashboard(String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin.fxml")); // Correct the path
            Parent root = fxmlLoader.load();

            // Set the current username in AdminDashboardController
            AdminDashboardController adminDashboardController = fxmlLoader.getController();
            adminDashboardController.setCurrentAdmin(username); // Pass the username here

            Stage stage = new Stage();
            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close login window
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load admin dashboard.");
        }
    }


}
