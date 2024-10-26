package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSetup {

    // Database URL
    private static final String URL = "jdbc:sqlite:test.db";

    // SQL statements to create table and insert data
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS books ("
            + "title TEXT, "
            + "authors TEXT, "
            + "physical_copies INTEGER, "
            + "price REAL, "
            + "sold_copies INTEGER"
            + ");";

    private static final String INSERT_DATA_SQL = "INSERT INTO books (title, authors, physical_copies, price, sold_copies) VALUES "
            + "(?, ?, ?, ?, ?);";

    // Sample data to insert
    private static final Object[][] BOOKS = {
            {"Absolute Java", "Savitch", 10, 50.0, 142},
            {"JAVA: How to Program", "Deitel and Deitel", 100, 89.0, 475},
            {"Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 99.0, 60},
            {"Java Software Solutions", "Lewis and Loftus", 70, 89.0, 12},
            {"Java Program Design", "Cohoon and Davidson", 2, 29.0, 86},
            {"Clean Code", "Robert Martin", 100, 300.0, 68},
            {"Gray Hat C#", "Brandon Perry", 1000, 600.0, 49},
            {"Python Basics", "David Amos", 600, 45.0, 300},
            {"Bayesian Statistics The Fun Way", "Will Kurt", 300, 178.0, 155}
    };

    public static void main(String[] args) {
        createTable();
        insertData();
    }

    private static void createTable() {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(CREATE_TABLE_SQL)) {

            pstmt.executeUpdate();
            System.out.println("Table 'books' created or already exists.");

        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    private static void insertData() {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_DATA_SQL)) {

            for (Object[] book : BOOKS) {
                pstmt.setString(1, (String) book[0]); // title
                pstmt.setString(2, (String) book[1]); // authors
                pstmt.setInt(3, (int) book[2]);       // physical_copies
                pstmt.setDouble(4, (double) book[3]); // price
                pstmt.setInt(5, (int) book[4]);       // sold_copies
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Book data inserted successfully.");

        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
}
