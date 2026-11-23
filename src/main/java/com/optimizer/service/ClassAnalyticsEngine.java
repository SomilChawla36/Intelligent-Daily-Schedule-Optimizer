package com.optimizer.service;

import com.optimizer.model.Mark;
import com.optimizer.model.StudentPerformance;
import com.optimizer.model.Subject;
import com.optimizer.model.SubjectStatistic;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Spring Service responsible for computing class-wide statistics using Java Stream API.
 * Demonstrates:
 * - @Service annotation
 * - Stream API: filter, mapToDouble, average, min, max, Collectors.groupingBy
 */
@Service
public class ClassAnalyticsEngine {

    public double calculateClassAveragePercentage(List<StudentPerformance> performances) {
        if (performances == null || performances.isEmpty()) return 0.0;
        return performances.stream()
                .mapToDouble(StudentPerformance::getPercentage)
                .average()
                .orElse(0.0);
    }

    public double findHighestClassPercentage(List<StudentPerformance> performances) {
        if (performances == null || performances.isEmpty()) return 0.0;
        return performances.stream()
                .mapToDouble(StudentPerformance::getPercentage)
                .max()
                .orElse(0.0);
    }

    public double findLowestClassPercentage(List<StudentPerformance> performances) {
        if (performances == null || performances.isEmpty()) return 0.0;
        return performances.stream()
                .mapToDouble(StudentPerformance::getPercentage)
                .min()
                .orElse(0.0);
    }

    public List<SubjectStatistic> calculateSubjectStatistics(List<StudentPerformance> performances) {
        if (performances == null || performances.isEmpty()) return Collections.emptyList();

        List<Mark> allMarks = performances.stream()
                .flatMap(p -> p.getMarksList().stream())
                .collect(Collectors.toList());

        Map<Subject, List<Mark>> marksBySubject = allMarks.stream()
                .filter(m -> m.getSubject() != null)
                .collect(Collectors.groupingBy(Mark::getSubject));

        List<SubjectStatistic> statsList = new ArrayList<>();
        for (Map.Entry<Subject, List<Mark>> entry : marksBySubject.entrySet()) {
            Subject subject = entry.getKey();
            List<Mark> marks = entry.getValue();

            double avgMarks = marks.stream().mapToDouble(Mark::getMarksObtained).average().orElse(0.0);
            double highMarks = marks.stream().mapToDouble(Mark::getMarksObtained).max().orElse(0.0);
            double lowMarks = marks.stream().mapToDouble(Mark::getMarksObtained).min().orElse(0.0);
            double avgPct = marks.stream().mapToDouble(Mark::getSubjectPercentage).average().orElse(0.0);

            statsList.add(new SubjectStatistic(
                    subject.getCode(),
                    subject.getName(),
                    avgMarks,
                    highMarks,
                    lowMarks,
                    avgPct
            ));
        }

        return statsList;
    }

    public Optional<SubjectStatistic> findHighestPerformingSubject(List<SubjectStatistic> stats) {
        if (stats == null || stats.isEmpty()) return Optional.empty();
        return stats.stream().max(Comparator.comparingDouble(SubjectStatistic::getAveragePercentage));
    }

    public Optional<SubjectStatistic> findLowestPerformingSubject(List<SubjectStatistic> stats) {
        if (stats == null || stats.isEmpty()) return Optional.empty();
        return stats.stream().min(Comparator.comparingDouble(SubjectStatistic::getAveragePercentage));
    }
}
