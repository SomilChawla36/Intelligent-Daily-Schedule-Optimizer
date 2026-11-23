package com.optimizer.dto;

import com.optimizer.model.SubjectStatistic;
import java.util.List;

/**
 * Clean JSON response sent back to the dashboard analytics page.
 */
public class ClassAnalyticsDTO {
    private long totalStudents;
    private double classAveragePercentage;
    private double highestPercentage;
    private double lowestPercentage;
    private String highestPerformingSubject;
    private String lowestPerformingSubject;
    private List<SubjectStatistic> subjectStatistics;

    public ClassAnalyticsDTO() {
    }

    public ClassAnalyticsDTO(long totalStudents, double classAveragePercentage, double highestPercentage,
                             double lowestPercentage, String highestPerformingSubject,
                             String lowestPerformingSubject, List<SubjectStatistic> subjectStatistics) {
        this.totalStudents = totalStudents;
        this.classAveragePercentage = classAveragePercentage;
        this.highestPercentage = highestPercentage;
        this.lowestPercentage = lowestPercentage;
        this.highestPerformingSubject = highestPerformingSubject;
        this.lowestPerformingSubject = lowestPerformingSubject;
        this.subjectStatistics = subjectStatistics;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public double getClassAveragePercentage() {
        return classAveragePercentage;
    }

    public void setClassAveragePercentage(double classAveragePercentage) {
        this.classAveragePercentage = classAveragePercentage;
    }

    public double getHighestPercentage() {
        return highestPercentage;
    }

    public void setHighestPercentage(double highestPercentage) {
        this.highestPercentage = highestPercentage;
    }

    public double getLowestPercentage() {
        return lowestPercentage;
    }

    public void setLowestPercentage(double lowestPercentage) {
        this.lowestPercentage = lowestPercentage;
    }

    public String getHighestPerformingSubject() {
        return highestPerformingSubject;
    }

    public void setHighestPerformingSubject(String highestPerformingSubject) {
        this.highestPerformingSubject = highestPerformingSubject;
    }

    public String getLowestPerformingSubject() {
        return lowestPerformingSubject;
    }

    public void setLowestPerformingSubject(String lowestPerformingSubject) {
        this.lowestPerformingSubject = lowestPerformingSubject;
    }

    public List<SubjectStatistic> getSubjectStatistics() {
        return subjectStatistics;
    }

    public void setSubjectStatistics(List<SubjectStatistic> subjectStatistics) {
        this.subjectStatistics = subjectStatistics;
    }
}
