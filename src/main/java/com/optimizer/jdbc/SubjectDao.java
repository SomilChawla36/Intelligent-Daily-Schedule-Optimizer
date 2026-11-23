package com.optimizer.jdbc;

import com.optimizer.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for Subject entity using JDBC.
 */
public class SubjectDao {

    public Subject save(Subject subject) throws SQLException {
        String sql = "INSERT INTO subjects (code, name, max_marks) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE name = VALUES(name), max_marks = VALUES(max_marks)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, subject.getCode());
            stmt.setString(2, subject.getName());
            stmt.setDouble(3, subject.getMaxMarks());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    subject.setId(generatedKeys.getLong(1));
                }
            }
            return subject;
        }
    }

    public List<Subject> findAll() throws SQLException {
        String sql = "SELECT id, code, name, max_marks FROM subjects ORDER BY code ASC";
        List<Subject> subjects = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subjects.add(new Subject(
                        rs.getLong("id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getDouble("max_marks")
                ));
            }
        }
        return subjects;
    }

    public Optional<Subject> findByCode(String code) throws SQLException {
        String sql = "SELECT id, code, name, max_marks FROM subjects WHERE code = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Subject(
                            rs.getLong("id"),
                            rs.getString("code"),
                            rs.getString("name"),
                            rs.getDouble("max_marks")
                    ));
                }
            }
        }
        return Optional.empty();
    }
}
