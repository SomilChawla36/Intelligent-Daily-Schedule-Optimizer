package com.optimizer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * JPA Entity to persist generated schedule slots into MySQL.
 */
@Entity
@Table(name = "study_schedules")
public class StudyScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "subject_code", nullable = false, length = 30)
    private String subjectCode;

    @Column(name = "subject_name", nullable = false, length = 100)
    private String subjectName;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "priority", nullable = false, length = 20)
    private String priority;

    @Column(name = "focus_area", columnDefinition = "TEXT")
    private String focusArea;

    @Column(name = "is_break")
    private boolean isBreak;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    public StudyScheduleEntity() {
        this.generatedAt = LocalDateTime.now();
    }

    public StudyScheduleEntity(Student student, String subjectCode, String subjectName,
                               int durationMinutes, String priority, String focusArea, boolean isBreak) {
        this.student = student;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.durationMinutes = durationMinutes;
        this.priority = priority;
        this.focusArea = focusArea;
        this.isBreak = isBreak;
        this.generatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = focusArea;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public void setBreak(boolean aBreak) {
        isBreak = aBreak;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }
}
