package com.optimizer.model;

/**
 * Encapsulates statistical metrics for an individual subject across the entire class.
 */
public class SubjectStatistic {
    private String subjectCode;
    private String subjectName;
    private double averageMarks;
    private double highestMarks;
    private double lowestMarks;
    private double averagePercentage;

    public SubjectStatistic(String subjectCode, String subjectName, double averageMarks,
                            double highestMarks, double lowestMarks, double averagePercentage) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.averageMarks = averageMarks;
        this.highestMarks = highestMarks;
        this.lowestMarks = lowestMarks;
        this.averagePercentage = averagePercentage;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public double getHighestMarks() {
        return highestMarks;
    }

    public double getLowestMarks() {
        return lowestMarks;
    }

    public double getAveragePercentage() {
        return averagePercentage;
    }

    @Override
    public String toString() {
        return String.format("%-20s | Avg: %5.1f (%5.1f%%) | High: %5.1f | Low: %5.1f",
                subjectName, averageMarks, averagePercentage, highestMarks, lowestMarks);
    }
}
