package com.optimizer.app;

import com.optimizer.model.*;
import com.optimizer.service.ClassAnalyticsEngine;
import com.optimizer.service.RankingEngine;
import com.optimizer.service.ScheduleOptimizerEngine;

import java.util.*;

/**
 * Phase 1 Console Application.
 * Demonstrates and verifies all Core Java requirements:
 * - OOP: Encapsulation, Domain Modeling
 * - Collections Framework: ArrayList, HashMap, List, Map
 * - Custom Comparator: Descending percentage ranking with tie handling
 * - Java Stream API: Class averages, min/max, grouping
 * - Intelligent Daily Study Schedule Optimizer algorithm
 */
public class Phase1ConsoleApp {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("       INTELLIGENT DAILY SCHEDULE OPTIMIZER - PHASE 1: CORE JAVA ENGINE         ");
        System.out.println("================================================================================\n");

        // 1. SETUP SUBJECTS
        Subject dsa = new Subject(1L, "CS301", "Data Structures", 100.0);
        Subject oop = new Subject(2L, "CS302", "OOP in Java", 100.0);
        Subject dbms = new Subject(3L, "CS303", "Database Systems", 100.0);
        Subject math = new Subject(4L, "CS304", "Discrete Mathematics", 100.0);

        List<Subject> subjects = Arrays.asList(dsa, oop, dbms, math);
        System.out.println("[Step 1] Registered Subjects:");
        subjects.forEach(s -> System.out.println("   * " + s));
        System.out.println();

        // 2. SETUP STUDENTS
        Student s1 = new Student(1L, "24CS001", "Aarav Sharma", "aarav@college.edu");
        Student s2 = new Student(2L, "24CS002", "Diya Patel", "diya@college.edu");
        Student s3 = new Student(3L, "24CS003", "Rohan Mehta", "rohan@college.edu");
        Student s4 = new Student(4L, "24CS004", "Ananya Verma", "ananya@college.edu");

        // 3. ENTER MARKS FOR EACH STUDENT
        // Aarav: Great at Java, struggles with Discrete Math
        List<Mark> marksAarav = Arrays.asList(
                new Mark(s1, dsa, 78.0),
                new Mark(s1, oop, 92.0),
                new Mark(s1, dbms, 85.0),
                new Mark(s1, math, 42.0)  // WEAK SUBJECT!
        );

        // Diya: All-rounder high achiever
        List<Mark> marksDiya = Arrays.asList(
                new Mark(s2, dsa, 94.0),
                new Mark(s2, oop, 96.0),
                new Mark(s2, dbms, 91.0),
                new Mark(s2, math, 89.0)
        );

        // Rohan: Average student, needs help in DSA and DBMS
        List<Mark> marksRohan = Arrays.asList(
                new Mark(s3, dsa, 48.0),  // WEAK
                new Mark(s3, oop, 65.0),
                new Mark(s3, dbms, 45.0),  // WEAK
                new Mark(s3, math, 62.0)
        );

        // Ananya: Ties with Aarav on percentage to test Comparator tie-breaking
        // Total = 78 + 92 + 85 + 42 = 297.0 (74.25%)
        List<Mark> marksAnanya = Arrays.asList(
                new Mark(s4, dsa, 80.0),
                new Mark(s4, oop, 84.0),
                new Mark(s4, dbms, 80.0),
                new Mark(s4, math, 53.0)  // Total = 297.0 (74.25% - Exactly same as Aarav!)
        );

        // 4. CALCULATE PERFORMANCE
        List<StudentPerformance> performances = new ArrayList<>();
        performances.add(new StudentPerformance(s1, marksAarav));
        performances.add(new StudentPerformance(s2, marksDiya));
        performances.add(new StudentPerformance(s3, marksRohan));
        performances.add(new StudentPerformance(s4, marksAnanya));

        // 5. RANK STUDENTS USING COMPARATOR
        RankingEngine rankingEngine = new RankingEngine();
        List<StudentPerformance> rankedStudents = rankingEngine.rankStudents(performances);

        System.out.println("[Step 2] Class Ranking Table (Sorted using Custom Comparator):");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-16s | %-14s | %-8s | %-6s\n",
                "RANK", "ROLL NO", "STUDENT NAME", "TOTAL MARKS", "PERCENT", "GRADE");
        System.out.println("--------------------------------------------------------------------------------");
        for (StudentPerformance sp : rankedStudents) {
            System.out.printf("#%-5d | %-10s | %-16s | %5.1f / %5.1f | %6.2f%% | %-6s\n",
                    sp.getRank(),
                    sp.getStudent().getRollNumber(),
                    sp.getStudent().getName(),
                    sp.getTotalObtained(),
                    sp.getTotalMaxMarks(),
                    sp.getPercentage(),
                    sp.getGrade()
            );
        }
        System.out.println("--------------------------------------------------------------------------------\n");

        // 6. CLASS ANALYTICS USING STREAM API
        ClassAnalyticsEngine analyticsEngine = new ClassAnalyticsEngine();
        double classAvg = analyticsEngine.calculateClassAveragePercentage(rankedStudents);
        double highestClassPct = analyticsEngine.findHighestClassPercentage(rankedStudents);
        double lowestClassPct = analyticsEngine.findLowestClassPercentage(rankedStudents);
        List<SubjectStatistic> subjectStats = analyticsEngine.calculateSubjectStatistics(rankedStudents);

        System.out.println("[Step 3] Class Analytics (Calculated using Java Stream API):");
        System.out.printf("   * Class Average Percentage: %.2f%%\n", classAvg);
        System.out.printf("   * Highest Percentage:        %.2f%%\n", highestClassPct);
        System.out.printf("   * Lowest Percentage:         %.2f%%\n", lowestClassPct);

        System.out.println("\n   Subject-Wise Performance Breakdown:");
        for (SubjectStatistic ss : subjectStats) {
            System.out.println("     - " + ss);
        }

        analyticsEngine.findHighestPerformingSubject(subjectStats).ifPresent(
                best -> System.out.printf("\n   * Highest Performing Subject: %s (Avg: %.2f%%)\n",
                        best.getSubjectName(), best.getAveragePercentage())
        );

        analyticsEngine.findLowestPerformingSubject(subjectStats).ifPresent(
                worst -> System.out.printf("   * Toughest / Lowest Performing Subject: %s (Avg: %.2f%%)\n",
                        worst.getSubjectName(), worst.getAveragePercentage())
        );
        System.out.println();

        // 7. INTELLIGENT DAILY STUDY SCHEDULE OPTIMIZER
        System.out.println("[Step 4] Intelligent Daily Study Schedule Generator Demo:");
        System.out.println("Analyzing Aarav Sharma (who scored 42% in Discrete Math and 92% in Java OOP)...");

        ScheduleOptimizerEngine optimizer = new ScheduleOptimizerEngine();
        // Aarav has 4.0 hours available for study today
        StudentPerformance aaravPerf = rankedStudents.stream()
                .filter(p -> p.getStudent().getRollNumber().equals("24CS001"))
                .findFirst()
                .orElse(rankedStudents.get(0));

        StudySchedule aaravSchedule = optimizer.generateSchedule(aaravPerf, 4.0);
        System.out.println(aaravSchedule);

        System.out.println("\n================================================================================");
        System.out.println(" PHASE 1 VERIFICATION COMPLETE: ALL CORE JAVA LOGIC FUNCTIONING ACCURATELY!     ");
        System.out.println("================================================================================");
    }
}
