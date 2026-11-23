package com.optimizer.dto;

/**
 * DTO returned after successful authentication.
 */
public class AuthResponseDTO {
    private boolean success;
    private String message;
    private String username;
    private String fullName;
    private String role;        // "FACULTY" or "STUDENT"
    private String rollNumber;  // Present for STUDENTS
    private String redirectUrl; // Directs to appropriate dashboard

    public AuthResponseDTO() {
    }

    public AuthResponseDTO(boolean success, String message, String username, String fullName,
                           String role, String rollNumber, String redirectUrl) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.rollNumber = rollNumber;
        this.redirectUrl = redirectUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
