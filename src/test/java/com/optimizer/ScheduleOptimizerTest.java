package com.optimizer;

import com.optimizer.comparator.StudentRankComparator;
import com.optimizer.model.*;
import com.optimizer.service.RankingEngine;
import com.optimizer.service.ScheduleOptimizerEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Phase 7: Verification Test Suite for the Intelligent Daily Schedule Optimizer.
 * Demonstrates:
 * - Automated Unit Testing with JUnit 5
 * - Testing algorithmic fairness, boundary values, and ranking ties
 */
public class ScheduleOptimizerTest {

    private ScheduleOptimizerEngine optimizer;
    private RankingEngine rankingEngine;
    private Subject dsa;
    private Subject oop;
    private Subject dbms;
    private Subject math;

    @BeforeEach
    void setUp() {
        optimizer = new ScheduleOptimizerEngine();
        rankingEngine = new RankingEngine();

        dsa = new Subject(1L, "CS301", "Data Structures", 100.0);
        oop = new Subject(2L, "CS302", "OOP in Java", 100.0);
        dbms = new Subject(3L, "CS303", "Database Systems", 100.0);
        math = new Subject(4L, "CS304", "Discrete Mathematics", 100.0);
    }

    @Test
    @DisplayName("Algorithm Test 1: Weaker subject receives significantly more minutes than strong subject")
    void testWeakerSubjectGetsMoreTime() {
        Student student = new Student("24CS001", "Aarav Sharma", "aarav@college.edu");
        List<Mark> marks = Arrays.asList(
                new Mark(student, math, 40.0), // WEAK: 40%
                new Mark(student, oop, 90.0)   // STRONG: 90%
        );

        StudentPerformance perf = new StudentPerformance(student, marks);
        StudySchedule schedule = optimizer.generateSchedule(perf, 4.0);

        assertNotNull(schedule);
        List<ScheduleSlot> slots = schedule.getSlots();
        assertFalse(slots.isEmpty());

        ScheduleSlot mathSlot = slots.stream()
                .filter(s -> s.getSubjectCode().equals("CS304"))
                .findFirst().orElseThrow();

        ScheduleSlot oopSlot = slots.stream()
                .filter(s -> s.getSubjectCode().equals("CS302"))
                .findFirst().orElseThrow();

        // Math deficit = 60, OOP deficit = 15 (floor) -> Math must get much more time!
        assertTrue(mathSlot.getDurationMinutes() > oopSlot.getDurationMinutes(),
                "Weaker subject (Math: 40%) must receive more time than strong subject (OOP: 90%)");
        assertEquals("HIGH", mathSlot.getPriority());
    }

    @Test
    @DisplayName("Algorithm Test 2: Rest breaks are correctly interleaved between subjects")
    void testRestBreaksInterleaved() {
        Student student = new Student("24CS002", "Diya Patel", "diya@college.edu");
        List<Mark> marks = Arrays.asList(
                new Mark(student, dsa, 80.0),
                new Mark(student, oop, 85.0),
                new Mark(student, dbms, 90.0)
        );

        StudentPerformance perf = new StudentPerformance(student, marks);
        StudySchedule schedule = optimizer.generateSchedule(perf, 3.0);

        List<ScheduleSlot> slots = schedule.getSlots();
        long breakCount = slots.stream().filter(ScheduleSlot::isBreak).count();

        // 3 subjects -> exactly 2 rest breaks between them
        assertEquals(2, breakCount, "For 3 subjects, exactly 2 rest breaks must be scheduled");
    }

    @Test
    @DisplayName("Algorithm Test 3: High scorer (100% in all) still receives minimum revision time")
    void testPerfectScorerStillGetsRevisionTime() {
        Student student = new Student("24CS003", "Perfect Student", "perfect@college.edu");
        List<Mark> marks = Arrays.asList(
                new Mark(student, dsa, 100.0),
                new Mark(student, math, 100.0)
        );

        StudentPerformance perf = new StudentPerformance(student, marks);
        StudySchedule schedule = optimizer.generateSchedule(perf, 2.0);

        for (ScheduleSlot slot : schedule.getSlots()) {
            if (!slot.isBreak()) {
                assertTrue(slot.getDurationMinutes() >= 20, "Every subject must receive at least 20 minutes of revision");
                assertEquals("LOW", slot.getPriority());
            }
        }
    }

    @Test
    @DisplayName("Algorithm Test 4: Equal percentage tie-breaker assigns equal rank fairly")
    void testRankingTieBreaker() {
        Student s1 = new Student("24CS001", "Student A", "a@college.edu");
        Student s2 = new Student("24CS002", "Student B", "b@college.edu");

        // Both score exactly 75.0%
        List<Mark> marksA = Arrays.asList(new Mark(s1, dsa, 75.0), new Mark(s1, math, 75.0));
        List<Mark> marksB = Arrays.asList(new Mark(s2, dsa, 75.0), new Mark(s2, math, 75.0));

        StudentPerformance p1 = new StudentPerformance(s1, marksA);
        StudentPerformance p2 = new StudentPerformance(s2, marksB);

        List<StudentPerformance> ranked = rankingEngine.rankStudents(Arrays.asList(p1, p2));

        assertEquals(1, ranked.get(0).getRank());
        assertEquals(1, ranked.get(1).getRank(), "Students with identical percentage must share the same rank");
    }
}
