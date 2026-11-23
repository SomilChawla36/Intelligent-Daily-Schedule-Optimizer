package com.optimizer.service;

import com.optimizer.model.Mark;
import com.optimizer.model.ScheduleSlot;
import com.optimizer.model.StudentPerformance;
import com.optimizer.model.StudySchedule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Spring Service: Intelligent Daily Schedule Optimizer.
 * Analyzes individual subject performance, calculates deficit weights,
 * and dynamically allocates daily study hours giving priority to weaker subjects.
 */
@Service
public class ScheduleOptimizerEngine {

    private static final int BREAK_DURATION_MINUTES = 10;

    public StudySchedule generateSchedule(StudentPerformance performance, double availableHours) {
        if (performance == null || performance.getStudent() == null) {
            throw new IllegalArgumentException("Student performance record cannot be null.");
        }
        if (availableHours <= 0 || availableHours > 16) {
            throw new IllegalArgumentException("Available daily study hours must be between 1 and 16.");
        }

        StudySchedule schedule = new StudySchedule(performance.getStudent(), availableHours);
        List<Mark> marks = performance.getMarksList();

        if (marks == null || marks.isEmpty()) {
            schedule.setSummaryAdvice("No subject marks found. Please enter subject marks to get a personalized schedule.");
            return schedule;
        }

        int totalAvailableMinutes = (int) Math.round(availableHours * 60);

        // 1. Sort marks in ASCENDING order of percentage (weakest subjects first)
        List<Mark> sortedMarks = new ArrayList<>(marks);
        sortedMarks.sort(Comparator.comparingDouble(Mark::getSubjectPercentage));

        // 2. Compute Deficit Weight for each subject
        double totalDeficitWeight = 0.0;
        List<Double> deficits = new ArrayList<>();
        for (Mark mark : sortedMarks) {
            double deficit = Math.max(15.0, 100.0 - mark.getSubjectPercentage());
            deficits.add(deficit);
            totalDeficitWeight += deficit;
        }

        // 3. Reserve 10-minute breaks between subjects
        int estimatedBreaks = Math.max(0, sortedMarks.size() - 1);
        int reservedBreakTime = estimatedBreaks * BREAK_DURATION_MINUTES;
        int netStudyMinutes = Math.max(30, totalAvailableMinutes - reservedBreakTime);

        // 4. Allocate minutes to each subject proportional to its deficit weight
        for (int i = 0; i < sortedMarks.size(); i++) {
            Mark mark = sortedMarks.get(i);
            double weight = deficits.get(i);
            int allocatedMinutes = (int) Math.round((weight / totalDeficitWeight) * netStudyMinutes);

            allocatedMinutes = Math.max(20, allocatedMinutes);

            String priority = determinePriority(mark.getSubjectPercentage());
            String focusAdvice = determineFocusAdvice(mark.getSubjectPercentage());

            schedule.addSlot(new ScheduleSlot(
                    mark.getSubject().getCode(),
                    mark.getSubject().getName(),
                    allocatedMinutes,
                    priority,
                    focusAdvice,
                    false
            ));

            if (i < sortedMarks.size() - 1) {
                schedule.addSlot(ScheduleSlot.createBreak(BREAK_DURATION_MINUTES));
            }
        }

        // 5. Generate overall summary advice
        Mark weakest = performance.getLowestScoringSubject();
        Mark strongest = performance.getHighestScoringSubject();

        String advice = String.format(
                "Focus most of your energy on %s (%.1f%%) where maximum score improvement is possible. " +
                "Keep %s (%.1f%%) sharp with active revision. Remember to strictly observe the short recharge breaks!",
                weakest != null ? weakest.getSubject().getName() : "your lowest subject",
                weakest != null ? weakest.getSubjectPercentage() : 0.0,
                strongest != null ? strongest.getSubject().getName() : "your highest subject",
                strongest != null ? strongest.getSubjectPercentage() : 0.0
        );
        schedule.setSummaryAdvice(advice);

        return schedule;
    }

    private String determinePriority(double pct) {
        if (pct < 50.0) return "HIGH";
        if (pct < 75.0) return "MEDIUM";
        return "LOW";
    }

    private String determineFocusAdvice(double pct) {
        if (pct < 50.0) {
            return "Re-read core concepts, solve textbook basic examples, and clear fundamental doubts.";
        } else if (pct < 75.0) {
            return "Practice intermediate problem sets, revise formulas, and attempt previous exam questions.";
        } else {
            return "Speed revision: formula sheets, summary flashcards, and quick timed quizzes.";
        }
    }
}
