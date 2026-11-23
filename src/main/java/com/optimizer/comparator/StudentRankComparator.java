package com.optimizer.comparator;

import com.optimizer.model.StudentPerformance;
import java.util.Comparator;

/**
 * Custom Comparator to rank students based on their academic percentage.
 * Demonstrates:
 * - Implementation of java.util.Comparator interface
 * - Descending order sorting logic
 * - Tie-breaker strategy for students with equal percentages
 */
public class StudentRankComparator implements Comparator<StudentPerformance> {

    @Override
    public int compare(StudentPerformance p1, StudentPerformance p2) {
        if (p1 == null && p2 == null) return 0;
        if (p1 == null) return 1;
        if (p2 == null) return -1;

        // 1. Primary Sort: Percentage in DESCENDING order (highest % first)
        int percentageComparison = Double.compare(p2.getPercentage(), p1.getPercentage());
        if (percentageComparison != 0) {
            return percentageComparison;
        }

        // 2. Secondary Sort (Tie-Breaker 1): Higher total marks obtained
        int totalComparison = Double.compare(p2.getTotalObtained(), p1.getTotalObtained());
        if (totalComparison != 0) {
            return totalComparison;
        }

        // 3. Tertiary Sort (Tie-Breaker 2): Ascending order of Roll Number
        if (p1.getStudent() != null && p2.getStudent() != null) {
            return p1.getStudent().getRollNumber().compareTo(p2.getStudent().getRollNumber());
        }

        return 0;
    }
}
