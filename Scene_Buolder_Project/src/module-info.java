module Scene_Builder_Project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
    
    // If you are using FXML files from the `application` package, you need to open the package
    opens application to javafx.fxml;
    exports application; // Add this line to export the application package

    
}
