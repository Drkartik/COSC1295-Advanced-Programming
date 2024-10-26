package application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrderCreation() {
        // Arrange
        Order order = new Order("1", "John Doe", "2024-10-01", 100.0);

        // Act & Assert
        assertEquals("1", order.getOrderNumber());
        assertEquals("John Doe", order.getCustomerName());
        assertEquals("2024-10-01", order.getOrderDate());
        assertEquals(100.0, order.getTotalPrice());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Order order = new Order("1", "John Doe", "2024-10-01", 100.0);
        
        // Act
        order.setCustomerName("Jane Doe");
        
        // Assert
        assertEquals("Jane Doe", order.getCustomerName());
    }
}
