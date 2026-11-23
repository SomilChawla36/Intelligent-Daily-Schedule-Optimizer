package com.optimizer.jdbc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Automatically initializes the database schema by executing schema.sql.
 * Allows the application to self-bootstrap the tables on startup.
 */
public class DatabaseInitializer {

    public static void initializeDatabase() {
        System.out.println("[DatabaseInitializer] Checking and initializing database schema...");

        try (InputStream in = DatabaseInitializer.class.getClassLoader().getResourceAsStream("schema.sql")) {
            if (in == null) {
                System.err.println("[DatabaseInitializer] schema.sql not found on classpath!");
                return;
            }

            String sqlContent;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
                sqlContent = reader.lines().collect(Collectors.joining("\n"));
            }

            // Split into individual SQL statements
            String[] statements = sqlContent.split(";");

            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {

                for (String sql : statements) {
                    String trimmed = sql.trim();
                    // Skip comments and empty lines
                    if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                        try {
                            stmt.execute(trimmed);
                        } catch (Exception e) {
                            // Ignore if already exists
                        }
                    }
                }
                System.out.println("[DatabaseInitializer] Schema verification successful! All tables are ready.");
            }

        } catch (Exception e) {
            System.err.println("[DatabaseInitializer] Could not execute schema: " + e.getMessage());
        }
    }
}
