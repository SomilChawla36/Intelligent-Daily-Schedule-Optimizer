package com.optimizer.service;

import com.optimizer.dto.AuthResponseDTO;
import com.optimizer.dto.LoginRequestDTO;
import com.optimizer.dto.SignupRequestDTO;
import com.optimizer.model.Student;
import com.optimizer.model.User;
import com.optimizer.repository.StudentRepository;
import com.optimizer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service managing user registration and authentication for both Faculty and Students.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public AuthService(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public AuthResponseDTO register(SignupRequestDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters long.");
        }
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email address is required.");
        }

        String username = dto.getUsername().trim().toLowerCase();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken. Please choose another.");
        }

        String role = (dto.getRole() != null && dto.getRole().equalsIgnoreCase("FACULTY"))
                ? "FACULTY" : "STUDENT";

        String rollNumber = null;
        if ("STUDENT".equals(role)) {
            if (dto.getRollNumber() == null || dto.getRollNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Roll Number is required for student registration.");
            }
            rollNumber = dto.getRollNumber().trim();

            if (userRepository.existsByRollNumber(rollNumber)) {
                throw new IllegalArgumentException("A user account already exists with Roll Number '" + rollNumber + "'.");
            }

            // Also register in Student database if not already there!
            if (!studentRepository.existsByRollNumber(rollNumber)) {
                studentRepository.save(new Student(rollNumber, dto.getFullName().trim(), dto.getEmail().trim()));
            }
        }

        User user = new User(
                username,
                dto.getPassword().trim(), // In a real production app we would BCrypt hash, but for 3rd sem simple text is easy to inspect
                dto.getFullName().trim(),
                dto.getEmail().trim(),
                role,
                rollNumber,
                dto.getDepartment() != null ? dto.getDepartment().trim() : ""
        );

        userRepository.save(user);

        String redirectUrl = "FACULTY".equals(role) ? "/index.html" : "/student-dashboard.html";
        return new AuthResponseDTO(
                true,
                "Account created successfully as " + role + "!",
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.getRollNumber(),
                redirectUrl
        );
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        String username = dto.getUsername().trim().toLowerCase();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        if (!user.getPassword().equals(dto.getPassword().trim())) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        String redirectUrl = user.isFaculty() ? "/index.html" : "/student-dashboard.html";

        return new AuthResponseDTO(
                true,
                "Login successful! Welcome back, " + user.getFullName(),
                user.getUsername(),
                user.getFullName(),
                user.getRole(),
                user.getRollNumber(),
                redirectUrl
        );
    }
}
