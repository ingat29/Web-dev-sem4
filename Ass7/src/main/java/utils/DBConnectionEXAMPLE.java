package main.java.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionEXAMPLE {
    // Update these credentials to match your local MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/puzzle_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_mysql_password";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // This forces Tomcat to load the MySQL driver into memory
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }
}