package com.optimizer.repository;

import com.optimizer.model.StudyScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for persisting generated study schedules.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<StudyScheduleEntity, Long> {

    List<StudyScheduleEntity> findByStudentIdOrderByGeneratedAtDesc(Long studentId);

    void deleteByStudentId(Long studentId);
}
