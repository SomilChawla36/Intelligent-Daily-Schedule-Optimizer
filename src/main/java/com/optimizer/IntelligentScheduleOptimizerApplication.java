package com.optimizer;

import com.optimizer.model.Mark;
import com.optimizer.model.Student;
import com.optimizer.model.Subject;
import com.optimizer.model.User;
import com.optimizer.repository.MarkRepository;
import com.optimizer.repository.StudentRepository;
import com.optimizer.repository.SubjectRepository;
import com.optimizer.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main Entry Point for the Intelligent Daily Schedule Optimizer.
 */
@SpringBootApplication
public class IntelligentScheduleOptimizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligentScheduleOptimizerApplication.class, args);
        System.out.println("\n================================================================================");
        System.out.println("  INTELLIGENT DAILY SCHEDULE OPTIMIZER - SERVER STARTED SUCCESSFULLY!           ");
        System.out.println("  Access the Portal at: http://localhost:8080/login.html                        ");
        System.out.println("================================================================================\n");
    }

    /**
     * Seeds initial academic subjects, sample students, and login accounts.
     */
    @Bean
    public CommandLineRunner seedInitialData(StudentRepository studentRepo,
                                             SubjectRepository subjectRepo,
                                             MarkRepository markRepo,
                                             UserRepository userRepo) {
        return args -> {
            // Seed Subjects
            if (subjectRepo.count() == 0) {
                System.out.println("[DataSeeder] Seeding default academic subjects...");
                Subject dsa = subjectRepo.save(new Subject("CS301", "Data Structures", 100.0));
                Subject oop = subjectRepo.save(new Subject("CS302", "OOP in Java", 100.0));
                Subject dbms = subjectRepo.save(new Subject("CS303", "Database Systems", 100.0));
                Subject math = subjectRepo.save(new Subject("CS304", "Discrete Mathematics", 100.0));

                if (studentRepo.count() == 0) {
                    System.out.println("[DataSeeder] Seeding initial student sample records...");
                    Student s1 = studentRepo.save(new Student("24CS001", "Aarav Sharma", "aarav@college.edu"));
                    Student s2 = studentRepo.save(new Student("24CS002", "Diya Patel", "diya@college.edu"));
                    Student s3 = studentRepo.save(new Student("24CS003", "Rohan Mehta", "rohan@college.edu"));

                    // Marks for Aarav
                    markRepo.save(new Mark(s1, dsa, 78.0));
                    markRepo.save(new Mark(s1, oop, 92.0));
                    markRepo.save(new Mark(s1, dbms, 85.0));
                    markRepo.save(new Mark(s1, math, 42.0)); // Weak subject

                    // Marks for Diya
                    markRepo.save(new Mark(s2, dsa, 94.0));
                    markRepo.save(new Mark(s2, oop, 96.0));
                    markRepo.save(new Mark(s2, dbms, 91.0));
                    markRepo.save(new Mark(s2, math, 89.0));

                    // Marks for Rohan
                    markRepo.save(new Mark(s3, dsa, 48.0));
                    markRepo.save(new Mark(s3, oop, 65.0));
                    markRepo.save(new Mark(s3, dbms, 45.0));
                    markRepo.save(new Mark(s3, math, 62.0));
                }
            }

            // Seed User Accounts for Faculty and Students
            if (userRepo.count() == 0) {
                System.out.println("[DataSeeder] Creating default login accounts...");

                // 1. Default Faculty Account
                userRepo.save(new User(
                        "faculty",
                        "admin123",
                        "Dr. Rajesh Sharma",
                        "faculty@college.edu",
                        "FACULTY",
                        null,
                        "Computer Science & Engineering"
                ));

                // 2. Default Student Accounts
                userRepo.save(new User(
                        "aarav",
                        "student123",
                        "Aarav Sharma",
                        "aarav@college.edu",
                        "STUDENT",
                        "24CS001",
                        null
                ));

                userRepo.save(new User(
                        "diya",
                        "student123",
                        "Diya Patel",
                        "diya@college.edu",
                        "STUDENT",
                        "24CS002",
                        null
                ));

                System.out.println("[DataSeeder] Initial user authentication accounts ready!");
            }
        };
    }
}
