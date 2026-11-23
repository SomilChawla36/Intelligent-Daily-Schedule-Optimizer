package com.optimizer.app;

import com.optimizer.jdbc.*;
import com.optimizer.model.*;
import com.optimizer.service.ClassAnalyticsEngine;
import com.optimizer.service.RankingEngine;
import com.optimizer.service.ScheduleOptimizerEngine;

import java.sql.SQLException;
import java.util.*;

/**
 * Phase 2 Application: Database Integration with raw JDBC.
 * Demonstrates:
 * - Connecting Java to MySQL using DriverManager
 * - Creating database tables via DDL execution
 * - Inserting and Querying records via PreparedStatement and ResultSet
 * - Bridging relational database data into our Java Domain Objects
 */
public class Phase2JdbcApp {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("      INTELLIGENT DAILY SCHEDULE OPTIMIZER - PHASE 2: DATABASE & JDBC           ");
        System.out.println("================================================================================\n");

        // Optional: override credentials via command-line arguments (e.g. java Phase2JdbcApp root mypass)
        if (args.length >= 2) {
            DatabaseConnection.setCredentials(args[0], args[1]);
            System.out.printf("[Config] Using provided credentials: User='%s'\n\n", args[0]);
        }

        StudentDao studentDao = new StudentDao();
        SubjectDao subjectDao = new SubjectDao();
        MarkDao markDao = new MarkDao();

        try {
            // 1. Initialize schema in MySQL
            DatabaseInitializer.initializeDatabase();
            System.out.println();

            // 2. Register Subjects in MySQL
            System.out.println("[Step 1] Inserting/Updating Subjects in MySQL via JDBC PreparedStatement...");
            Subject dsa = subjectDao.save(new Subject("CS301", "Data Structures", 100.0));
            Subject oop = subjectDao.save(new Subject("CS302", "OOP in Java", 100.0));
            Subject dbms = subjectDao.save(new Subject("CS303", "Database Systems", 100.0));
            Subject math = subjectDao.save(new Subject("CS304", "Discrete Mathematics", 100.0));

            List<Subject> allSubjects = subjectDao.findAll();
            System.out.println("   Successfully persisted subjects in MySQL:");
            allSubjects.forEach(s -> System.out.printf("     - ID: %d | %s (%s, Max: %.0f)\n",
                    s.getId(), s.getName(), s.getCode(), s.getMaxMarks()));
            System.out.println();

            // 3. Register Students in MySQL
            System.out.println("[Step 2] Inserting Students in MySQL via StudentDao...");
            // Use findByRollNumber to avoid duplicate key errors if run multiple times
            Student s1 = getOrCreateStudent(studentDao, "24CS001", "Aarav Sharma", "aarav@college.edu");
            Student s2 = getOrCreateStudent(studentDao, "24CS002", "Diya Patel", "diya@college.edu");
            Student s3 = getOrCreateStudent(studentDao, "24CS003", "Rohan Mehta", "rohan@college.edu");

            List<Student> allStudents = studentDao.findAll();
            System.out.println("   Total students registered in MySQL: " + allStudents.size());
            allStudents.forEach(st -> System.out.printf("     - ID: %d | Roll: %s | Name: %s\n",
                    st.getId(), st.getRollNumber(), st.getName()));
            System.out.println();

            // 4. Save Marks in MySQL
            System.out.println("[Step 3] Storing Student Marks in MySQL via MarkDao (Relational JOIN Table)...");
            // Aarav: High in Java, struggling in Math
            markDao.saveOrUpdate(s1.getId(), dsa.getId(), 78.0);
            markDao.saveOrUpdate(s1.getId(), oop.getId(), 92.0);
            markDao.saveOrUpdate(s1.getId(), dbms.getId(), 85.0);
            markDao.saveOrUpdate(s1.getId(), math.getId(), 42.0);

            // Diya: High marks across all subjects
            markDao.saveOrUpdate(s2.getId(), dsa.getId(), 94.0);
            markDao.saveOrUpdate(s2.getId(), oop.getId(), 96.0);
            markDao.saveOrUpdate(s2.getId(), dbms.getId(), 91.0);
            markDao.saveOrUpdate(s2.getId(), math.getId(), 89.0);

            // Rohan: Average marks
            markDao.saveOrUpdate(s3.getId(), dsa.getId(), 48.0);
            markDao.saveOrUpdate(s3.getId(), oop.getId(), 65.0);
            markDao.saveOrUpdate(s3.getId(), dbms.getId(), 45.0);
            markDao.saveOrUpdate(s3.getId(), math.getId(), 62.0);
            System.out.println("   Marks successfully persisted in MySQL!\n");

            // 5. Query Marks back from MySQL and reconstruct StudentPerformance objects
            System.out.println("[Step 4] Querying Relational Data from MySQL into Java Performance Objects...");
            List<StudentPerformance> performances = new ArrayList<>();
            for (Student st : allStudents) {
                List<Mark> marks = markDao.findMarksByStudentId(st.getId(), st);
                if (!marks.isEmpty()) {
                    performances.add(new StudentPerformance(st, marks));
                }
            }

            // 6. Rank Students using our Java Comparator
            RankingEngine rankingEngine = new RankingEngine();
            List<StudentPerformance> ranked = rankingEngine.rankStudents(performances);

            System.out.println("\n[Step 5] Database-Driven Class Rankings:");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-6s | %-10s | %-16s | %-14s | %-8s | %-6s\n",
                    "RANK", "ROLL NO", "STUDENT NAME", "TOTAL MARKS", "PERCENT", "GRADE");
            System.out.println("--------------------------------------------------------------------------------");
            for (StudentPerformance sp : ranked) {
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

            // 7. Generate Optimized Study Timetable from DB data
            System.out.println("[Step 6] Running Intelligent Schedule Optimizer on MySQL Data for Aarav Sharma...");
            ScheduleOptimizerEngine optimizer = new ScheduleOptimizerEngine();
            StudentPerformance aaravPerf = ranked.stream()
                    .filter(p -> p.getStudent().getRollNumber().equals("24CS001"))
                    .findFirst()
                    .orElse(ranked.get(0));

            StudySchedule schedule = optimizer.generateSchedule(aaravPerf, 4.0);
            System.out.println(schedule);

            System.out.println("\n================================================================================");
            System.out.println(" PHASE 2 VERIFICATION COMPLETE: DATABASE & JDBC FULLY FUNCTIONAL!               ");
            System.out.println("================================================================================");

        } catch (SQLException e) {
            System.err.println("\n[PHASE 2 DATABASE NOTICE]: " + e.getMessage());
            System.err.println("\nPlease check your MySQL root password.");
            System.err.println("You can configure the password in: src/main/resources/db.properties");
            System.err.println("Or run with your password as an argument:");
            System.err.println("   java -cp \"bin;lib/mysql-connector-j-8.3.0.jar;src/main/resources\" com.optimizer.app.Phase2JdbcApp root <YOUR_PASSWORD>");
        }
    }

    private static Student getOrCreateStudent(StudentDao dao, String roll, String name, String email) throws SQLException {
        Optional<Student> existing = dao.findByRollNumber(roll);
        if (existing.isPresent()) {
            return existing.get();
        }
        return dao.save(new Student(roll, name, email));
    }
}
