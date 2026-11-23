package com.optimizer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the full personalized daily timetable generated for a student.
 */
public class StudySchedule {
    private Student student;
    private double totalAvailableHours;
    private List<ScheduleSlot> slots;
    private String summaryAdvice;

    public StudySchedule(Student student, double totalAvailableHours) {
        this.student = student;
        this.totalAvailableHours = totalAvailableHours;
        this.slots = new ArrayList<>();
    }

    public void addSlot(ScheduleSlot slot) {
        this.slots.add(slot);
    }

    public Student getStudent() {
        return student;
    }

    public double getTotalAvailableHours() {
        return totalAvailableHours;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=================================================================\n"));
        sb.append(String.format(" PERSONALIZED DAILY STUDY SCHEDULE FOR %s (Roll: %s)\n",
                student != null ? student.getName().toUpperCase() : "STUDENT",
                student != null ? student.getRollNumber() : "N/A"));
        sb.append(String.format(" Available Study Time: %.1f Hours\n", totalAvailableHours));
        sb.append(String.format("=================================================================\n"));
        for (int i = 0; i < slots.size(); i++) {
            sb.append(String.format("%2d. %s\n", (i + 1), slots.get(i)));
        }
        sb.append(String.format("-----------------------------------------------------------------\n"));
        sb.append("Advice: ").append(summaryAdvice).append("\n");
        sb.append(String.format("================================================================="));
        return sb.toString();
    }
}
