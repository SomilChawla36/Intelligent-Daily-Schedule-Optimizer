package com.optimizer.service;

import com.optimizer.exception.ResourceNotFoundException;
import com.optimizer.model.Subject;
import com.optimizer.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service managing academic courses/subjects.
 */
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectByCode(String code) {
        return subjectRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with code: " + code));
    }

    @Transactional
    public Subject addSubject(Subject subject) {
        if (subject.getCode() == null || subject.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject code is required.");
        }
        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name is required.");
        }
        if (subjectRepository.existsByCode(subject.getCode().trim())) {
            throw new IllegalArgumentException("Subject with code '" + subject.getCode() + "' already exists.");
        }
        if (subject.getMaxMarks() <= 0) {
            subject.setMaxMarks(100.0);
        }
        return subjectRepository.save(subject);
    }
}
