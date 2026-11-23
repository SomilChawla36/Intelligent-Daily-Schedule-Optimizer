package com.optimizer.controller;

import com.optimizer.dto.MarkEntryDTO;
import com.optimizer.model.Mark;
import com.optimizer.model.StudentPerformance;
import com.optimizer.service.MarkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Marks Management & Academic Performance Calculations.
 * Base Path: /api/marks
 */
@RestController
@RequestMapping("/api/marks")
public class MarkController {

    private final MarkService markService;

    public MarkController(MarkService markService) {
        this.markService = markService;
    }

    /**
     * POST /api/marks - Record or update marks for a student.
     */
    @PostMapping
    public ResponseEntity<Mark> enterMark(@RequestBody MarkEntryDTO dto) {
        Mark saved = markService.saveOrUpdateMark(dto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    /**
     * GET /api/marks/student/{rollNumber} - Get subject marks for a student.
     */
    @GetMapping("/student/{rollNumber}")
    public ResponseEntity<List<Mark>> getMarksByRollNumber(@PathVariable String rollNumber) {
        return ResponseEntity.ok(markService.getMarksByStudentRollNumber(rollNumber));
    }

    /**
     * GET /api/marks/performance/{rollNumber} - Get computed performance metrics for a single student.
     */
    @GetMapping("/performance/{rollNumber}")
    public ResponseEntity<StudentPerformance> getPerformance(@PathVariable String rollNumber) {
        return ResponseEntity.ok(markService.getPerformanceForStudent(rollNumber));
    }

    /**
     * GET /api/marks/performances - Get performance metrics for all students.
     */
    @GetMapping("/performances")
    public ResponseEntity<List<StudentPerformance>> getAllPerformances() {
        return ResponseEntity.ok(markService.getAllStudentPerformances());
    }
}
