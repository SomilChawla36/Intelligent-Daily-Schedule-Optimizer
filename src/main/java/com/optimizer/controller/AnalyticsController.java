package com.optimizer.controller;

import com.optimizer.dto.ClassAnalyticsDTO;
import com.optimizer.model.StudentPerformance;
import com.optimizer.model.SubjectStatistic;
import com.optimizer.service.ClassAnalyticsEngine;
import com.optimizer.service.MarkService;
import com.optimizer.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Class-Wide Academic Analytics.
 * Base Path: /api/analytics
 * Demonstrates:
 * - Using Java Stream API to compute analytics across the entire student population
 */
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final StudentService studentService;
    private final MarkService markService;
    private final ClassAnalyticsEngine analyticsEngine;

    public AnalyticsController(StudentService studentService,
                               MarkService markService,
                               ClassAnalyticsEngine analyticsEngine) {
        this.studentService = studentService;
        this.markService = markService;
        this.analyticsEngine = analyticsEngine;
    }

    /**
     * GET /api/analytics - Class statistical summary (average, high, low, subject stats).
     */
    @GetMapping
    public ResponseEntity<ClassAnalyticsDTO> getClassAnalytics() {
        long totalStudents = studentService.getTotalStudentCount();
        List<StudentPerformance> performances = markService.getAllStudentPerformances();

        double classAvg = analyticsEngine.calculateClassAveragePercentage(performances);
        double highestPct = analyticsEngine.findHighestClassPercentage(performances);
        double lowestPct = analyticsEngine.findLowestClassPercentage(performances);
        List<SubjectStatistic> subjectStats = analyticsEngine.calculateSubjectStatistics(performances);

        String bestSubject = analyticsEngine.findHighestPerformingSubject(subjectStats)
                .map(SubjectStatistic::getSubjectName)
                .orElse("N/A");

        String toughestSubject = analyticsEngine.findLowestPerformingSubject(subjectStats)
                .map(SubjectStatistic::getSubjectName)
                .orElse("N/A");

        ClassAnalyticsDTO dto = new ClassAnalyticsDTO(
                totalStudents,
                classAvg,
                highestPct,
                lowestPct,
                bestSubject,
                toughestSubject,
                subjectStats
        );

        return ResponseEntity.ok(dto);
    }
}
