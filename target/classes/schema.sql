-- ==============================================================================
-- DATABASE SCHEMA: Intelligent Daily Schedule Optimizer
-- Database Engine: MySQL 8.0+
-- Description: Normalized tables with constraints, foreign keys, and indexes.
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS schedule_optimizer_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE schedule_optimizer_db;

-- 1. Students Table
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Subjects Table
CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    max_marks DOUBLE NOT NULL DEFAULT 100.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Marks Table (Links Student + Subject + Score)
CREATE TABLE IF NOT EXISTS marks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    marks_obtained DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_marks_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_marks_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    CONSTRAINT uq_student_subject UNIQUE (student_id, subject_id)
) ENGINE=InnoDB;

-- 4. Study Schedules Table (Persists generated personalized timetables)
CREATE TABLE IF NOT EXISTS study_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    subject_code VARCHAR(30) NOT NULL,
    subject_name VARCHAR(100) NOT NULL,
    duration_minutes INT NOT NULL,
    priority VARCHAR(20) NOT NULL,
    focus_area TEXT,
    is_break BOOLEAN DEFAULT FALSE,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_schedule_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB;
