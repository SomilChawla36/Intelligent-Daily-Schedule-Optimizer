package com.optimizer.service;

import com.optimizer.dto.MarkEntryDTO;
import com.optimizer.exception.InvalidMarksException;
import com.optimizer.exception.ResourceNotFoundException;
import com.optimizer.model.Mark;
import com.optimizer.model.Student;
import com.optimizer.model.StudentPerformance;
import com.optimizer.model.Subject;
import com.optimizer.repository.MarkRepository;
import com.optimizer.repository.StudentRepository;
import com.optimizer.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service managing marks entry, score validation, and linking
 * marks to student performance metrics.
 */
@Service
public class MarkService {

    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public MarkService(MarkRepository markRepository,
                       StudentRepository studentRepository,
                       SubjectRepository subjectRepository) {
        this.markRepository = markRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    @Transactional
    public Mark saveOrUpdateMark(MarkEntryDTO dto) {
        if (dto.getRollNumber() == null || dto.getRollNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("Student roll number is required.");
        }
        if (dto.getSubjectCode() == null || dto.getSubjectCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code is required.");
        }

        Student student = studentRepository.findByRollNumber(dto.getRollNumber().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Roll Number: " + dto.getRollNumber()));

        Subject subject = subjectRepository.findByCode(dto.getSubjectCode().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with code: " + dto.getSubjectCode()));

        if (dto.getMarksObtained() < 0) {
            throw new InvalidMarksException("Marks cannot be negative: " + dto.getMarksObtained());
        }
        if (dto.getMarksObtained() > subject.getMaxMarks()) {
            throw new InvalidMarksException(String.format(
                    "Marks obtained (%.2f) cannot exceed maximum allowed marks (%.2f) for %s.",
                    dto.getMarksObtained(), subject.getMaxMarks(), subject.getName()
            ));
        }

        Optional<Mark> existing = markRepository.findByStudentIdAndSubjectId(student.getId(), subject.getId());
        Mark mark;
        if (existing.isPresent()) {
            mark = existing.get();
            mark.setMarksObtained(dto.getMarksObtained());
        } else {
            mark = new Mark(student, subject, dto.getMarksObtained());
        }

        return markRepository.save(mark);
    }

    public List<Mark> getMarksByStudentRollNumber(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Roll Number: " + rollNumber));
        return markRepository.findByStudentId(student.getId());
    }

    public StudentPerformance getPerformanceForStudent(String rollNumber) {
        Student student = studentRepository.findByRollNumber(rollNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with Roll Number: " + rollNumber));
        List<Mark> marks = markRepository.findByStudentId(student.getId());
        return new StudentPerformance(student, marks);
    }

    /**
     * Builds StudentPerformance objects for every registered student in the database.
     */
    public List<StudentPerformance> getAllStudentPerformances() {
        List<Student> students = studentRepository.findAll();
        List<StudentPerformance> performances = new ArrayList<>();

        for (Student st : students) {
            List<Mark> marks = markRepository.findByStudentId(st.getId());
            performances.add(new StudentPerformance(st, marks));
        }
        return performances;
    }
}
