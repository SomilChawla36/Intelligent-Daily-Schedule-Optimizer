package com.optimizer.controller;

import com.optimizer.model.Subject;
import com.optimizer.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Academic Subjects.
 * Base Path: /api/subjects
 */
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * GET /api/subjects - List all registered subjects.
     */
    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    /**
     * POST /api/subjects - Add a new subject.
     */
    @PostMapping
    public ResponseEntity<Subject> addSubject(@RequestBody Subject subject) {
        Subject created = subjectService.addSubject(subject);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
