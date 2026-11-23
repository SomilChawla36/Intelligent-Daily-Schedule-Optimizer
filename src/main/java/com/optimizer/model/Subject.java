package com.optimizer.model;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Represents an Academic Subject in the system.
 * Demonstrates:
 * - JPA Entity Mapping
 * - Column Validation
 */
@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 30)
    private String code;       // e.g. "CS301"

    @Column(name = "name", nullable = false, length = 100)
    private String name;       // e.g. "Data Structures"

    @Column(name = "max_marks", nullable = false)
    private double maxMarks = 100.0;

    public Subject() {
    }

    public Subject(Long id, String code, String name, double maxMarks) {
        if (maxMarks <= 0) {
            throw new IllegalArgumentException("Maximum marks must be greater than zero.");
        }
        this.id = id;
        this.code = code;
        this.name = name;
        this.maxMarks = maxMarks;
    }

    public Subject(String code, String name, double maxMarks) {
        this(null, code, name, maxMarks);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(double maxMarks) {
        if (maxMarks <= 0) {
            throw new IllegalArgumentException("Maximum marks must be greater than zero.");
        }
        this.maxMarks = maxMarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(code, subject.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, Max: %.0f)", name, code, maxMarks);
    }
}
