package com.optimizer.dto;

/**
 * DTO for User Registration (supports both Faculty and Student signup).
 */
public class SignupRequestDTO {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;        // "FACULTY" or "STUDENT"
    private String rollNumber;  // Required if role is STUDENT
    private String department;  // Optional for FACULTY

    public SignupRequestDTO() {
    }

    public SignupRequestDTO(String username, String password, String fullName, String email,
                            String role, String rollNumber, String department) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.rollNumber = rollNumber;
        this.department = department;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
