package com.optimizer.dto;

/**
 * Data Transfer Object for creating or updating a Student.
 */
public class StudentDTO {
    private Long id;
    private String rollNumber;
    private String name;
    private String email;

    public StudentDTO() {
    }

    public StudentDTO(Long id, String rollNumber, String name, String email) {
        this.id = id;
        this.rollNumber = rollNumber;
        this.name = name;
        this.email = email;
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
}
