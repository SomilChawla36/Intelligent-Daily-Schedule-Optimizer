package com.optimizer.controller;

import com.optimizer.dto.ScheduleRequestDTO;
import com.optimizer.dto.ScheduleResponseDTO;
import com.optimizer.model.StudentPerformance;
import com.optimizer.model.StudySchedule;
import com.optimizer.model.StudyScheduleEntity;
import com.optimizer.repository.ScheduleRepository;
import com.optimizer.service.MarkService;
import com.optimizer.service.ScheduleOptimizerEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Intelligent Daily Study Schedule Optimizer.
 * Base Path: /api/schedule
 */
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final MarkService markService;
    private final ScheduleOptimizerEngine scheduleOptimizer;
    private final ScheduleRepository scheduleRepository;

    public ScheduleController(MarkService markService,
                              ScheduleOptimizerEngine scheduleOptimizer,
                              ScheduleRepository scheduleRepository) {
        this.markService = markService;
        this.scheduleOptimizer = scheduleOptimizer;
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * POST /api/schedule/generate - Generate personalized daily study schedule.
     */
    @PostMapping("/generate")
    public ResponseEntity<ScheduleResponseDTO> generateSchedule(@RequestBody ScheduleRequestDTO request) {
        double hours = request.getAvailableDailyHours() > 0 ? request.getAvailableDailyHours() : 4.0;
        return ResponseEntity.ok(processScheduleGeneration(request.getRollNumber(), hours));
    }

    /**
     * GET /api/schedule/{rollNumber}?hours=4.0 - Convenience GET endpoint for easy browser testing.
     */
    @GetMapping("/{rollNumber}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleByRollNumber(
            @PathVariable String rollNumber,
            @RequestParam(defaultValue = "4.0") double hours) {
        return ResponseEntity.ok(processScheduleGeneration(rollNumber, hours));
    }

    private ScheduleResponseDTO processScheduleGeneration(String rollNumber, double hours) {
        StudentPerformance performance = markService.getPerformanceForStudent(rollNumber);
        StudySchedule schedule = scheduleOptimizer.generateSchedule(performance, hours);

        // Optionally persist schedule to database
        try {
            scheduleRepository.deleteByStudentId(performance.getStudent().getId());
            schedule.getSlots().forEach(slot -> {
                scheduleRepository.save(new StudyScheduleEntity(
                        performance.getStudent(),
                        slot.getSubjectCode(),
                        slot.getSubjectName(),
                        slot.getDurationMinutes(),
                        slot.getPriority(),
                        slot.getFocusArea(),
                        slot.isBreak()
                ));
            });
        } catch (Exception ignored) {
            // Non-critical: transient DB save
        }

        return new ScheduleResponseDTO(
                performance.getStudent().getName(),
                performance.getStudent().getRollNumber(),
                schedule.getTotalAvailableHours(),
                schedule.getSlots(),
                schedule.getSummaryAdvice()
        );
    }
}
