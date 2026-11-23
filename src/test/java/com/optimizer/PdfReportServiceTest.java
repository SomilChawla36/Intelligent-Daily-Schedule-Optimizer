package com.optimizer;

import com.optimizer.model.*;
import com.optimizer.service.MarkService;
import com.optimizer.service.PdfReportService;
import com.optimizer.service.RankingEngine;
import com.optimizer.service.ScheduleOptimizerEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Test for PDF Performance Report Generation.
 * Uses pure Java Subclassing (Polymorphism) instead of dynamic bytecode agents,
 * ensuring 100% compatibility across all JDK releases.
 */
public class PdfReportServiceTest {

    static class StubMarkService extends MarkService {
        private final List<StudentPerformance> stubPerformances;

        public StubMarkService(List<StudentPerformance> performances) {
            super(null, null, null);
            this.stubPerformances = performances;
        }

        @Override
        public List<StudentPerformance> getAllStudentPerformances() {
            return stubPerformances;
        }
    }

    @Test
    @DisplayName("PDF Test: Generates valid PDF byte stream with %PDF header")
    void testPdfReportGeneration() {
        Student student = new Student("24CS001", "Aarav Sharma", "aarav@college.edu");
        Subject math = new Subject(1L, "CS304", "Discrete Mathematics", 100.0);
        Subject oop = new Subject(2L, "CS302", "OOP in Java", 100.0);

        List<Mark> marks = Arrays.asList(
                new Mark(student, math, 42.0),
                new Mark(student, oop, 92.0)
        );

        StudentPerformance perf = new StudentPerformance(student, marks);
        MarkService stubMarkService = new StubMarkService(Collections.singletonList(perf));
        RankingEngine rankingEngine = new RankingEngine();
        ScheduleOptimizerEngine optimizer = new ScheduleOptimizerEngine();

        PdfReportService pdfService = new PdfReportService(stubMarkService, rankingEngine, optimizer);
        byte[] pdfBytes = pdfService.generatePerformanceReportPdf("24CS001");

        assertNotNull(pdfBytes, "PDF byte array must not be null");
        assertTrue(pdfBytes.length > 500, "PDF size must be greater than 500 bytes");

        // Verify PDF Magic Bytes: First 4 bytes must be "%PDF"
        String magicHeader = new String(pdfBytes, 0, 4);
        assertEquals("%PDF", magicHeader, "File must start with standard PDF magic header '%PDF'");
    }
}
