package com.optimizer.dto;

/**
 * Data Transfer Object for entering or updating a student's marks.
 */
public class MarkEntryDTO {
    private String rollNumber;
    private String subjectCode;
    private double marksObtained;

    public MarkEntryDTO() {
    }

    public MarkEntryDTO(String rollNumber, String subjectCode, double marksObtained) {
        this.rollNumber = rollNumber;
        this.subjectCode = subjectCode;
        this.marksObtained = marksObtained;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public double getMarksObtained() {
        return marksObtained;
    }

    public void setMarksObtained(double marksObtained) {
        this.marksObtained = marksObtained;
    }
}
