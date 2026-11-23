package com.optimizer.controller;

import com.optimizer.model.StudentPerformance;
import com.optimizer.service.MarkService;
import com.optimizer.service.RankingEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Student Rankings.
 * Base Path: /api/rankings
 * Demonstrates:
 * - Dynamic ranking using Java Comparator on database data
 */
@RestController
@RequestMapping("/api/rankings")
public class RankingController {

    private final MarkService markService;
    private final RankingEngine rankingEngine;

    public RankingController(MarkService markService, RankingEngine rankingEngine) {
        this.markService = markService;
        this.rankingEngine = rankingEngine;
    }

    /**
     * GET /api/rankings - Returns all students sorted in descending order of percentage.
     */
    @GetMapping
    public ResponseEntity<List<StudentPerformance>> getClassRankings() {
        List<StudentPerformance> allPerformances = markService.getAllStudentPerformances();
        List<StudentPerformance> ranked = rankingEngine.rankStudents(allPerformances);
        return ResponseEntity.ok(ranked);
    }
}
