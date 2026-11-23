package com.optimizer.jdbc;

import com.optimizer.model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for Student entity using raw JDBC.
 * Demonstrates:
 * - PreparedStatement to protect against SQL Injection
 * - Mapping ResultSet rows into Java Student objects
 * - Retrieving auto-generated primary keys
 * - Try-with-resources for automatic resource closing
 */
public class StudentDao {

    /**
     * Inserts a new student and sets the generated database ID.
     */
    public Student save(Student student) throws SQLException {
        String sql = "INSERT INTO students (roll_number, name, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getRollNumber());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getEmail());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getLong(1));
                }
            }
            return student;
        }
    }

    /**
     * Retrieves all students from the database.
     */
    public List<Student> findAll() throws SQLException {
        String sql = "SELECT id, roll_number, name, email FROM students ORDER BY roll_number ASC";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }
        }
        return students;
    }

    /**
     * Finds a student by their unique roll number.
     */
    public Optional<Student> findByRollNumber(String rollNumber) throws SQLException {
        String sql = "SELECT id, roll_number, name, email FROM students WHERE roll_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, rollNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToStudent(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Deletes a student by ID.
     */
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Helper method to map a single SQL ResultSet row to a Java Student object.
     */
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getLong("id"),
                rs.getString("roll_number"),
                rs.getString("name"),
                rs.getString("email")
        );
    }
}
