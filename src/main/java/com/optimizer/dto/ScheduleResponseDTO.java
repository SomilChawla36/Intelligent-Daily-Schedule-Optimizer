package com.optimizer.dto;

import com.optimizer.model.ScheduleSlot;
import java.util.List;

/**
 * Clean JSON response sent back to the frontend containing
 * the generated study timetable and personalized advice.
 */
public class ScheduleResponseDTO {
    private String studentName;
    private String rollNumber;
    private double availableHours;
    private List<ScheduleSlot> slots;
    private String summaryAdvice;

    public ScheduleResponseDTO() {
    }

    public ScheduleResponseDTO(String studentName, String rollNumber, double availableHours,
                               List<ScheduleSlot> slots, String summaryAdvice) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.availableHours = availableHours;
        this.slots = slots;
        this.summaryAdvice = summaryAdvice;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(double availableHours) {
        this.availableHours = availableHours;
    }

    public List<ScheduleSlot> getSlots() {
        return slots;
    }

    public void setSlots(List<ScheduleSlot> slots) {
        this.slots = slots;
    }

    public String getSummaryAdvice() {
        return summaryAdvice;
    }

    public void setSummaryAdvice(String summaryAdvice) {
        this.summaryAdvice = summaryAdvice;
    }
}
