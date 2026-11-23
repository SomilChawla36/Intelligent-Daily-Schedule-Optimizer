package com.optimizer.controller;

import com.optimizer.dto.StudentDTO;
import com.optimizer.model.Student;
import com.optimizer.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Student Management.
 * Base Path: /api/students
 * Demonstrates:
 * - @RestController (Combines @Controller + @ResponseBody for JSON responses)
 * - HTTP Methods: GET, POST, PUT, DELETE
 * - Proper HTTP Status Codes (200 OK, 201 Created)
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * GET /api/students - View all registered students.
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    /**
     * GET /api/students/{rollNumber} - Find a specific student by roll number.
     */
    @GetMapping("/{rollNumber}")
    public ResponseEntity<Student> getStudentByRollNumber(@PathVariable String rollNumber) {
        return ResponseEntity.ok(studentService.getStudentByRollNumber(rollNumber));
    }

    /**
     * POST /api/students - Register a new student.
     */
    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody StudentDTO dto) {
        Student created = studentService.addStudent(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /api/students/{id} - Update student profile.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody StudentDTO dto) {
        Student updated = studentService.updateStudent(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/students/{id} - Delete student record.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Student deleted successfully with ID: " + id);
        return ResponseEntity.ok(response);
    }
}
