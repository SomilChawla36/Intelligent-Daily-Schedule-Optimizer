package com.optimizer.service;

import com.optimizer.dto.StudentDTO;
import com.optimizer.exception.ResourceNotFoundException;
import com.optimizer.model.Student;
import com.optimizer.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service managing student profiles and registration.
 * Demonstrates:
 * - @Service annotation (Spring managed singleton component)
 * - Dependency Injection via constructor
 * - Business validation before persistence
 * - @Transactional operations
 */
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
    }

    public Student getStudentByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Roll Number: " + rollNumber));
    }

    @Transactional
    public Student addStudent(StudentDTO dto) {
        if (dto.getRollNumber() == null || dto.getRollNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Roll number is required.");
        }
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name is required.");
        }
        if (studentRepository.existsByRollNumber(dto.getRollNumber().trim())) {
            throw new IllegalArgumentException("A student with Roll Number '" + dto.getRollNumber() + "' already exists.");
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            if (studentRepository.existsByEmail(dto.getEmail().trim())) {
                throw new IllegalArgumentException("A student with Email '" + dto.getEmail() + "' already exists.");
            }
        }

        Student student = new Student(
                dto.getRollNumber().trim(),
                dto.getName().trim(),
                dto.getEmail() != null ? dto.getEmail().trim() : ""
        );
        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, StudentDTO dto) {
        Student existing = getStudentById(id);

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            existing.setName(dto.getName().trim());
        }
        if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
            existing.setEmail(dto.getEmail().trim());
        }
        // Roll number can also be updated if it does not clash
        if (dto.getRollNumber() != null && !dto.getRollNumber().trim().isEmpty()) {
            String newRoll = dto.getRollNumber().trim();
            if (!newRoll.equals(existing.getRollNumber()) && studentRepository.existsByRollNumber(newRoll)) {
                throw new IllegalArgumentException("Roll Number '" + newRoll + "' is already in use.");
            }
            existing.setRollNumber(newRoll);
        }

        return studentRepository.save(existing);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete: Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    public long getTotalStudentCount() {
        return studentRepository.count();
    }
}
