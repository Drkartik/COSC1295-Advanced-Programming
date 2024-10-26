package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EditOrderController {

    @FXML
    private TextField totalPriceField; // TextField to enter the new total price

    @FXML
    private TextField orderDateField; // TextField for order date (optional)

    @FXML
    private Button saveButton; // Button to save changes

    private String orderNumber; // Holds the order number being edited
    private Order order; // Represents the current order being edited

    public void setOrder(String orderNumber, Order currentOrder) {
        this.orderNumber = orderNumber;
        this.order = currentOrder; // Get the current order passed from the controller

        // Set the current details in the UI
        if (order != null) {
            totalPriceField.setText(String.valueOf(order.getTotalPrice()));
            orderDateField.setText(order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        } else {
            showAlert(Alert.AlertType.ERROR, "Order Not Found", "The specified order could not be found.");
            ((Stage) saveButton.getScene().getWindow()).close(); // Close the dialog if order is not found
        }
    }

    @FXML
    private void handleSave() {
        // Validate inputs
        String newTotalPrice = totalPriceField.getText();
        String newOrderDate = orderDateField.getText();

        if (newTotalPrice.isEmpty() || newOrderDate.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill in all fields.");
            return;
        }

        // Update the order details
        double updatedPrice;
        try {
            updatedPrice = Double.parseDouble(newTotalPrice);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Total Price must be a valid number.");
            return;
        }

        // Assuming orderDate is a LocalDateTime, convert string back to LocalDateTime
        LocalDateTime updatedOrderDate;
        try {
            updatedOrderDate = LocalDateTime.parse(newOrderDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Order Date must be in the format yyyy-MM-dd HH:mm.");
            return;
        }

        // Update the order details in memory
        order.setTotalPrice(updatedPrice);
        order.setOrderDate(updatedOrderDate);

        // Notify the user and close the dialog
        showAlert(Alert.AlertType.INFORMATION, "Order Updated", "Order " + orderNumber + " updated successfully.");
        ((Stage) saveButton.getScene().getWindow()).close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
