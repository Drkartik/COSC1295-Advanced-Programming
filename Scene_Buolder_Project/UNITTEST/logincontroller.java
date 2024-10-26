package application;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginController loginController;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginController = new LoginController();
        
        // Initialize mock fields
        loginController.usernameField = new TextField();
        loginController.passwordField = new PasswordField();
    }

    @Test
    void testSuccessfulAdminLogin() throws Exception {
        // Arrange
        loginController.usernameField.setText("admin_user");
        loginController.passwordField.setText("admin_pass");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("username")).thenReturn("admin_user");
        when(mockResultSet.getString("role")).thenReturn("admin");

        loginController.setDatabaseConnection(mockConnection);

        // Act
        loginController.handleLoginButtonAction(null);

        // Assert
        assertEquals("admin", loginController.getCurrentUserRole());
    }

    @Test
    void testFailedLoginDueToInvalidCredentials() throws Exception {
        // Arrange
        loginController.usernameField.setText("invalid_user");
        loginController.passwordField.setText("wrong_pass");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        
        when(mockResultSet.next()).thenReturn(false);

        loginController.setDatabaseConnection(mockConnection);

        // Act
        loginController.handleLoginButtonAction(null);

        // Assert
        assertNull(loginController.getCurrentUserRole());
    }
}
