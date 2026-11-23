package com.optimizer.repository;

import com.optimizer.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for Mark entity.
 */
@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

    List<Mark> findByStudentId(Long studentId);

    Optional<Mark> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    @Query("SELECT m FROM Mark m JOIN FETCH m.subject WHERE m.student.rollNumber = :rollNumber")
    List<Mark> findByStudentRollNumber(@Param("rollNumber") String rollNumber);

    void deleteByStudentId(Long studentId);
}
