package com.optimizer.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Manages JDBC connections to the MySQL database.
 * Demonstrates:
 * - Singleton pattern / centralized connection management
 * - Reading external configuration from properties files
 * - Proper SQLException handling with beginner-friendly diagnostics
 */
public class DatabaseConnection {

    private static String url = "jdbc:mysql://localhost:3306/schedule_optimizer_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static String user = "root";
    private static String password = "root";

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                Properties prop = new Properties();
                prop.load(input);
                if (prop.getProperty("db.url") != null) url = prop.getProperty("db.url");
                if (prop.getProperty("db.user") != null) user = prop.getProperty("db.user");
                if (prop.getProperty("db.password") != null) password = prop.getProperty("db.password");
            }
        } catch (Exception e) {
            System.err.println("[DatabaseConnection] Using default database configuration.");
        }
    }

    public static void setCredentials(String dbUser, String dbPassword) {
        user = dbUser;
        password = dbPassword;
    }

    /**
     * Opens and returns a new JDBC Connection to MySQL.
     * Uses try-with-resources at call sites to prevent connection leaks.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly load MySQL JDBC driver class for compatibility across JDKs
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            // In modern JDBC 4.0+, DriverManager automatically detects drivers on the classpath
        }

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            System.err.println("\n[JDBC CONNECTION ERROR] Unable to connect to MySQL database!");
            System.err.println("   URL:  " + url);
            System.err.println("   User: " + user);
            System.err.println("   Tip:  Ensure MySQL is running and verify the password in 'src/main/resources/db.properties'.");
            throw ex;
        }
    }
}
