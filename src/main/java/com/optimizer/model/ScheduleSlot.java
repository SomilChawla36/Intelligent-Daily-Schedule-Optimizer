package com.optimizer.model;

/**
 * Represents a single time block in a student's daily study schedule.
 */
public class ScheduleSlot {
    private String subjectCode;
    private String subjectName;
    private int durationMinutes;
    private String priority;      // "HIGH", "MEDIUM", "LOW", or "BREAK"
    private String focusArea;     // Specific recommendation for this slot
    private boolean isBreak;

    public ScheduleSlot(String subjectCode, String subjectName, int durationMinutes,
                        String priority, String focusArea, boolean isBreak) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.durationMinutes = durationMinutes;
        this.priority = priority;
        this.focusArea = focusArea;
        this.isBreak = isBreak;
    }

    public static ScheduleSlot createBreak(int minutes) {
        return new ScheduleSlot(
                "BREAK",
                "Rest & Refreshment Break",
                minutes,
                "RELAX",
                "Hydrate, stretch, or take a short walk to recharge your brain.",
                true
        );
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getPriority() {
        return priority;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public boolean isBreak() {
        return isBreak;
    }

    @Override
    public String toString() {
        if (isBreak) {
            return String.format("[BREAK] %d mins - %s", durationMinutes, focusArea);
        }
        return String.format("[Priority: %-6s] %-20s (%d mins) -> %s",
                priority, subjectName, durationMinutes, focusArea);
    }
}
