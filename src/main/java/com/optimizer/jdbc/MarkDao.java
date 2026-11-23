package com.optimizer.jdbc;

import com.optimizer.model.Mark;
import com.optimizer.model.Student;
import com.optimizer.model.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for Marks using JDBC with relational SQL JOINs.
 * Demonstrates:
 * - SQL JOIN queries between marks, students, and subjects
 * - UPSERT (INSERT ... ON DUPLICATE KEY UPDATE)
 */
public class MarkDao {

    /**
     * Saves or updates a mark entry for a student in a specific subject.
     */
    public void saveOrUpdate(Long studentId, Long subjectId, double marksObtained) throws SQLException {
        String sql = "INSERT INTO marks (student_id, subject_id, marks_obtained) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE marks_obtained = VALUES(marks_obtained)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, studentId);
            stmt.setLong(2, subjectId);
            stmt.setDouble(3, marksObtained);

            stmt.executeUpdate();
        }
    }

    /**
     * Retrieves all marks for a student by their database ID, joining Subject data.
     */
    public List<Mark> findMarksByStudentId(Long studentId, Student student) throws SQLException {
        String sql = "SELECT m.id AS mark_id, m.marks_obtained, " +
                     "s.id AS subject_id, s.code AS subject_code, s.name AS subject_name, s.max_marks " +
                     "FROM marks m " +
                     "JOIN subjects s ON m.subject_id = s.id " +
                     "WHERE m.student_id = ? " +
                     "ORDER BY s.code ASC";

        List<Mark> marks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subject subject = new Subject(
                            rs.getLong("subject_id"),
                            rs.getString("subject_code"),
                            rs.getString("subject_name"),
                            rs.getDouble("max_marks")
                    );

                    Mark mark = new Mark(
                            rs.getLong("mark_id"),
                            student,
                            subject,
                            rs.getDouble("marks_obtained")
                    );
                    marks.add(mark);
                }
            }
        }
        return marks;
    }
}
