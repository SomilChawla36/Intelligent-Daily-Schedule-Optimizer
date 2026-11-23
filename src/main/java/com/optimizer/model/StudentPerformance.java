package com.optimizer.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Encapsulates the complete academic performance calculation for a single student.
 * Demonstrates:
 * - Business logic calculation inside domain models
 * - Java Collections (List<Mark>)
 * - Java Stream API for min/max subject detection
 */
public class StudentPerformance {
    private Student student;
    private List<Mark> marksList;

    private double totalObtained;
    private double totalMaxMarks;
    private double percentage;
    private String grade;
    private Mark highestScoringSubject;
    private Mark lowestScoringSubject;
    private int rank;

    public StudentPerformance(Student student, List<Mark> marksList) {
        this.student = student;
        this.marksList = marksList != null ? marksList : new ArrayList<>();
        calculate();
    }

    /**
     * Performs automatic calculation of total, percentage, grade,
     * and identifies strongest/weakest subjects using Stream API.
     */
    public void calculate() {
        if (marksList.isEmpty()) {
            this.totalObtained = 0.0;
            this.totalMaxMarks = 0.0;
            this.percentage = 0.0;
            this.grade = "N/A";
            this.highestScoringSubject = null;
            this.lowestScoringSubject = null;
            return;
        }

        // 1. Calculate totals using Java Stream API
        this.totalObtained = marksList.stream()
                .mapToDouble(Mark::getMarksObtained)
                .sum();

        this.totalMaxMarks = marksList.stream()
                .mapToDouble(m -> m.getSubject() != null ? m.getSubject().getMaxMarks() : 100.0)
                .sum();

        // 2. Percentage
        this.percentage = totalMaxMarks > 0 ? (totalObtained / totalMaxMarks) * 100.0 : 0.0;

        // 3. Grade assignment based on standard academic grading
        this.grade = computeGrade(this.percentage);

        // 4. Find Highest and Lowest scoring subject by subject percentage
        // Comparator based on getSubjectPercentage()
        Comparator<Mark> subjectScoreComparator = Comparator.comparingDouble(Mark::getSubjectPercentage);

        this.highestScoringSubject = marksList.stream()
                .max(subjectScoreComparator)
                .orElse(null);

        this.lowestScoringSubject = marksList.stream()
                .min(subjectScoreComparator)
                .orElse(null);
    }

    private String computeGrade(double pct) {
        if (pct >= 90.0) return "A+";
        if (pct >= 80.0) return "A";
        if (pct >= 70.0) return "B";
        if (pct >= 60.0) return "C";
        if (pct >= 50.0) return "D";
        if (pct >= 40.0) return "E";
        return "F (Fail)";
    }

    // Getters and Setters
    public Student getStudent() {
        return student;
    }

    public List<Mark> getMarksList() {
        return marksList;
    }

    public void setMarksList(List<Mark> marksList) {
        this.marksList = marksList;
        calculate();
    }

    public double getTotalObtained() {
        return totalObtained;
    }

    public double getTotalMaxMarks() {
        return totalMaxMarks;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getGrade() {
        return grade;
    }

    public Mark getHighestScoringSubject() {
        return highestScoringSubject;
    }

    public Mark getLowestScoringSubject() {
        return lowestScoringSubject;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return String.format(
            "Roll: %s | Name: %-15s | Total: %.1f/%.1f | %6.2f%% | Grade: %-3s | Rank: #%d",
            student != null ? student.getRollNumber() : "N/A",
            student != null ? student.getName() : "N/A",
            totalObtained,
            totalMaxMarks,
            percentage,
            grade,
            rank
        );
    }
}
