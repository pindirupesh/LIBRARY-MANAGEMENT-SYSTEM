package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Unit V: JDBC - this class only creates the connection to the MySQL database.
public class DBConnection {

    // Change USER and PASSWORD to your own MySQL username and password
    private static final String URL = "jdbc:mysql://localhost:3306/librarydb";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Returns a new connection to librarydb using DriverManager
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
