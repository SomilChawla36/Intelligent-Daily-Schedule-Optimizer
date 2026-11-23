package com.optimizer.model;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Represents a Student in the system.
 * Demonstrates:
 * - Spring Data JPA Entity Mapping
 * - Table & Column Constraints
 * - Encapsulation
 */
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "roll_number", nullable = false, unique = true, length = 50)
    private String rollNumber;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // Default constructor required by JPA
    public Student() {
    }

    public Student(Long id, String rollNumber, String name, String email) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
    }

    public Student(String rollNumber, String name, String email) {
        this(null, rollNumber, name, email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(rollNumber, student.rollNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rollNumber);
    }

    @Override
    public String toString() {
        return String.format("Student[ID: %d, Roll: %s, Name: %s, Email: %s]", id, rollNumber, name, email);
    }
}
