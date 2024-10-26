package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import java.io.IOException;
import java.util.List;

public class ViewOrdersController {

    @FXML
    private TableView<Order> ordersTable; // TableView for displaying orders

    @FXML
    private TableColumn<Order, String> orderIdColumn; // Order ID column
    @FXML
    private TableColumn<Order, String> customerNameColumn; // Customer Name column
    @FXML
    private TableColumn<Order, String> orderDateColumn; // Order Date column
    @FXML
    private TableColumn<Order, Double> totalAmountColumn; // Total Amount column

    @FXML
    private Button editOrderButton; // Reference to the edit button
    @FXML
    private Button deleteOrderButton; // Reference to the delete button
    @FXML
    private Button backButton; // Reference to the back button

    @FXML
    private void initialize() {
        // Set up the table columns
        orderIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderNumber()));
        customerNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerName()));
        orderDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrderDate().toString()));
        totalAmountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
    }

    public void setOrders(List<Order> orders) {
        ordersTable.getItems().clear(); // Clear the current items in the table
        ordersTable.getItems().addAll(orders); // Add the new orders to the table
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) backButton.getScene().getWindow(); // Get the current stage
        stage.close(); // Close the current stage
    }


    @FXML
    private void handleEditOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to edit.");
            return;
        }

        String orderNumber = selectedOrder.getOrderNumber();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_order.fxml"));
            Parent root = loader.load();

            EditOrderController editOrderController = loader.getController();
            // Pass both the order number and the selected order
            editOrderController.setOrder(orderNumber, selectedOrder); // Update here

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Order");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open edit order screen.");
        }
    }


    @FXML
    private void handleDeleteOrder() {
        Order selectedOrder = ordersTable.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order to delete.");
            return;
        }

        String orderNumber = selectedOrder.getOrderNumber();
        
        // Implement your deletion logic here, e.g., remove from database
        System.out.println("Deleting order: " + orderNumber);
        
        // Remove from TableView
        ordersTable.getItems().remove(selectedOrder);
        
        showAlert(Alert.AlertType.INFORMATION, "Order Deleted", "Order " + orderNumber + " deleted successfully.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
