package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditProfileController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label usernameLabel;

    private String currentUsername;

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        usernameLabel.setText("Username: " + username);
        loadUserProfile();
    }

    private void loadUserProfile() {
        String url = "jdbc:sqlite:test.db"; 
        String sql = "SELECT first_name, last_name FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                firstNameField.setText(rs.getString("first_name"));
                lastNameField.setText(rs.getString("last_name"));
                System.out.println("User profile loaded for username: " + currentUsername);
            } else {
                System.out.println("User profile not found for username: " + currentUsername);
                showAlert(AlertType.ERROR, "Profile Error", "User profile not found.");
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "SQL Exception: " + e.getMessage());
        }
    }

    @FXML
    public void handleSaveProfile() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String newPassword = passwordField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            showAlert(AlertType.ERROR, "Input Error", "First Name and Last Name must be filled out.");
            return;
        }

        boolean success = saveUserProfile(firstName, lastName, newPassword);

        Alert alert = new Alert(success ? AlertType.INFORMATION : AlertType.ERROR);
        alert.setTitle(success ? "Profile Update" : "Update Failed");
        alert.setContentText(success ? "Profile updated successfully!" : "Failed to update profile. Check the logs for details.");
        alert.showAndWait();

        if (success) {
            navigateToDashboard();
        }
    }

    public boolean saveUserProfile(String firstName, String lastName, String newPassword) {
        String url = "jdbc:sqlite:test.db";
        String sql = newPassword.isEmpty()
                ? "UPDATE users SET first_name = ?, last_name = ? WHERE username = ?"
                : "UPDATE users SET first_name = ?, last_name = ?, password = ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);

            if (newPassword.isEmpty()) {
                pstmt.setString(3, currentUsername);
            } else {
                pstmt.setString(3, newPassword);
                pstmt.setString(4, currentUsername);
            }

            // Debug output
            System.out.println("Attempting to update profile for username: " + currentUsername);
            System.out.println("Executing SQL: " + pstmt.toString());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("No rows updated - Username may not exist: " + currentUsername);
            }
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "SQL Exception: " + e.getMessage());
            return false;
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = fxmlLoader.load();

            // Set the current username in DashboardController
            DashboardController dashboardController = fxmlLoader.getController();
            dashboardController.setCurrentUsername(currentUsername); // Corrected variable name here

            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) firstNameField.getScene().getWindow(); // Use firstNameField to get the current stage
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
