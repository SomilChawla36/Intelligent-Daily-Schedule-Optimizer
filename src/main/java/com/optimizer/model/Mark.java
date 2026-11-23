package com.optimizer.model;

import com.optimizer.exception.InvalidMarksException;
import jakarta.persistence.*;

/**
 * Represents the marks obtained by a Student in a particular Subject.
 * Demonstrates:
 * - JPA Relationships (@ManyToOne, @JoinColumn)
 * - Unique constraint across student_id and subject_id
 * - In-model business logic validation
 */
@Entity
@Table(name = "marks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"student_id", "subject_id"})
})
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "marks_obtained", nullable = false)
    private double marksObtained;

    public Mark() {
    }

    public Mark(Long id, Student student, Subject subject, double marksObtained) {
        validate(subject, marksObtained);
        this.id = id;
        this.student = student;
        this.subject = subject;
        this.marksObtained = marksObtained;
    }

    public Mark(Student student, Subject subject, double marksObtained) {
        this(null, student, subject, marksObtained);
    }

    private void validate(Subject subject, double marks) {
        if (marks < 0) {
            throw new InvalidMarksException("Marks cannot be negative: " + marks);
        }
        if (subject != null && marks > subject.getMaxMarks()) {
            throw new InvalidMarksException(String.format(
                "Marks obtained (%.2f) cannot exceed maximum subject marks (%.2f) for %s.",
                marks, subject.getMaxMarks(), subject.getName()
            ));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        validate(subject, this.marksObtained);
        this.subject = subject;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        validate(this.subject, marksObtained);
        this.marksObtained = marksObtained;
    }

    public double getSubjectPercentage() {
        if (subject == null || subject.getMaxMarks() == 0) return 0.0;
        return (marksObtained / subject.getMaxMarks()) * 100.0;
    }

    @Override
    public String toString() {
        return String.format("%s: %.1f / %.1f (%.1f%%)",
            subject != null ? subject.getName() : "Unknown",
            marksObtained,
            subject != null ? subject.getMaxMarks() : 100.0,
            getSubjectPercentage()
        );
    }
}
