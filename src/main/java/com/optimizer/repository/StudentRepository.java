package com.optimizer.repository;

import com.optimizer.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for Student entity.
 * Demonstrates:
 * - Spring Data JPA magic: Inherits save(), findById(), findAll(), deleteById()
 * - Derived Query Methods: generated automatically by method name!
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRollNumber(String rollNumber);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmail(String email);
}
