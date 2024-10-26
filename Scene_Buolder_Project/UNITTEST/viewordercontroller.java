package application;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewOrdersControllerTest {

    private ViewOrdersController viewOrdersController;

    @BeforeEach
    void setUp() {
        viewOrdersController = new ViewOrdersController();
        
        // Initialize mock TableView and TableColumn
        viewOrdersController.ordersTable = new TableView<>();
        viewOrdersController.orderIdColumn = new TableColumn<>("Order ID");
        viewOrdersController.customerNameColumn = new TableColumn<>("Customer Name");
        viewOrdersController.orderDateColumn = new TableColumn<>("Order Date");
        viewOrdersController.totalAmountColumn = new TableColumn<>("Total Amount");

        viewOrdersController.ordersTable.getColumns().addAll(
                viewOrdersController.orderIdColumn,
                viewOrdersController.customerNameColumn,
                viewOrdersController.orderDateColumn,
                viewOrdersController.totalAmountColumn
        );
    }

    @Test
    void testSetOrders() {
        // Arrange
        List<Order> orders = Arrays.asList(
                new Order("1", "John Doe", "2024-10-01", 100.0),
                new Order("2", "Jane Doe", "2024-10-02", 200.0)
        );

        // Act
        viewOrdersController.setOrders(orders);

        // Assert
        assertEquals(2, viewOrdersController.ordersTable.getItems().size());
        assertEquals("1", viewOrdersController.ordersTable.getItems().get(0).getOrderNumber());
        assertEquals("John Doe", viewOrdersController.ordersTable.getItems().get(0).getCustomerName());
    }

    @Test
    void testInitializeTableColumns() {
        // Arrange
        Order order = new Order("1", "John Doe", "2024-10-01", 100.0);
        viewOrdersController.ordersTable.getItems().add(order);

        // Act
        viewOrdersController.initialize();

        // Assert
        assertEquals("1", viewOrdersController.ordersTable.getItems().get(0).getOrderNumber());
    }
}
