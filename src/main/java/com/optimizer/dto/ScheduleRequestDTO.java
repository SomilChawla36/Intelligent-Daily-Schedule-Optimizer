package com.optimizer.dto;

/**
 * Request payload when a student asks to generate an optimized study schedule.
 */
public class ScheduleRequestDTO {
    private String rollNumber;
    private double availableDailyHours; // e.g. 4.0

    public ScheduleRequestDTO() {
    }

    public ScheduleRequestDTO(String rollNumber, double availableDailyHours) {
        this.rollNumber = rollNumber;
        this.availableDailyHours = availableDailyHours;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public double getAvailableDailyHours() {
        return availableDailyHours;
    }

    public void setAvailableDailyHours(double availableDailyHours) {
        this.availableDailyHours = availableDailyHours;
    }
}
