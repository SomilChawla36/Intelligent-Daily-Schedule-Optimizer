package com.optimizer.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.optimizer.model.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service to generate downloadable, professional PDF performance report cards using OpenPDF.
 * Demonstrates:
 * - PDF document generation with OpenPDF / iText
 * - Working with in-memory ByteStreams (ByteArrayOutputStream)
 * - Formatting tables, cell padding, borders, and colors in Java
 */
@Service
public class PdfReportService {

    private final MarkService markService;
    private final RankingEngine rankingEngine;
    private final ScheduleOptimizerEngine scheduleOptimizer;

    // Palette Colors matching our Modern Blue & White theme
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color DARK_BLUE = new Color(30, 64, 175);
    private static final Color LIGHT_BG = new Color(239, 246, 255);
    private static final Color BORDER_GRAY = new Color(226, 232, 240);
    private static final Color TEXT_DARK = new Color(15, 23, 42);

    public PdfReportService(MarkService markService,
                            RankingEngine rankingEngine,
                            ScheduleOptimizerEngine scheduleOptimizer) {
        this.markService = markService;
        this.rankingEngine = rankingEngine;
        this.scheduleOptimizer = scheduleOptimizer;
    }

    /**
     * Generates a complete PDF report card as a byte array for download.
     *
     * @param rollNumber Roll number of the student
     * @return byte[] containing the raw PDF document data
     */
    public byte[] generatePerformanceReportPdf(String rollNumber) {
        // 1. Fetch ranked performances to determine official class rank
        List<StudentPerformance> rankedPerformances = rankingEngine.rankStudents(markService.getAllStudentPerformances());
        StudentPerformance performance = rankedPerformances.stream()
                .filter(p -> p.getStudent() != null && p.getStudent().getRollNumber().equalsIgnoreCase(rollNumber))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Student not found with roll number: " + rollNumber));

        // 2. Generate a personalized 4.0-hour schedule for the report
        StudySchedule schedule = scheduleOptimizer.generateSchedule(performance, 4.0);

        // 3. Create PDF Document in memory
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Fonts
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, PRIMARY_BLUE);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(100, 116, 139));
            Font sectionHeadingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, DARK_BLUE);
            Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 10, TEXT_DARK);
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, TEXT_DARK);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

            // 4. HEADER
            Paragraph title = new Paragraph("INTELLIGENT DAILY SCHEDULE OPTIMIZER", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Official Academic Performance & Study Recommendation Report", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(15);
            document.add(subtitle);

            // 5. STUDENT PROFILE SUMMARY TABLE
            PdfPTable infoTable = new PdfPTable(4);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(15);

            addInfoCell(infoTable, "Student Name:", performance.getStudent().getName(), regularFont, boldFont);
            addInfoCell(infoTable, "Roll Number:", performance.getStudent().getRollNumber(), regularFont, boldFont);
            addInfoCell(infoTable, "Official Class Rank:", "#" + performance.getRank(), regularFont, boldFont);
            addInfoCell(infoTable, "Overall Grade:", performance.getGrade(), regularFont, boldFont);
            addInfoCell(infoTable, "Total Scored:", String.format("%.1f / %.1f", performance.getTotalObtained(), performance.getTotalMaxMarks()), regularFont, boldFont);
            addInfoCell(infoTable, "Overall Percentage:", String.format("%.2f%%", performance.getPercentage()), regularFont, boldFont);
            addInfoCell(infoTable, "Date of Report:", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")), regularFont, boldFont);
            addInfoCell(infoTable, "Academic Year:", "3rd Semester", regularFont, boldFont);

            document.add(infoTable);

            // 6. SUBJECT-WISE EXAMINATION MARKS TABLE
            Paragraph section1 = new Paragraph("1. Examination Marks Breakdown", sectionHeadingFont);
            section1.setSpacingAfter(8);
            document.add(section1);

            PdfPTable marksTable = new PdfPTable(new float[]{2f, 4.5f, 2.5f, 2.5f, 2.5f});
            marksTable.setWidthPercentage(100);
            marksTable.setSpacingAfter(18);

            // Table Headers
            addHeaderCell(marksTable, "Code", tableHeaderFont);
            addHeaderCell(marksTable, "Subject Name", tableHeaderFont);
            addHeaderCell(marksTable, "Marks Scored", tableHeaderFont);
            addHeaderCell(marksTable, "Max Marks", tableHeaderFont);
            addHeaderCell(marksTable, "Percentage", tableHeaderFont);

            // Table Rows
            for (Mark m : performance.getMarksList()) {
                addBodyCell(marksTable, m.getSubject().getCode(), regularFont, Element.ALIGN_CENTER);
                addBodyCell(marksTable, m.getSubject().getName(), boldFont, Element.ALIGN_LEFT);
                addBodyCell(marksTable, String.format("%.1f", m.getMarksObtained()), regularFont, Element.ALIGN_CENTER);
                addBodyCell(marksTable, String.format("%.1f", m.getSubject().getMaxMarks()), regularFont, Element.ALIGN_CENTER);
                addBodyCell(marksTable, String.format("%.1f%%", m.getSubjectPercentage()), boldFont, Element.ALIGN_CENTER);
            }
            document.add(marksTable);

            // 7. ACADEMIC EVALUATION & HIGHLIGHTS
            Paragraph section2 = new Paragraph("2. Academic Analysis & Subject Insights", sectionHeadingFont);
            section2.setSpacingAfter(8);
            document.add(section2);

            String highestSub = performance.getHighestScoringSubject() != null
                    ? String.format("%s (%.1f%%)", performance.getHighestScoringSubject().getSubject().getName(), performance.getHighestScoringSubject().getSubjectPercentage())
                    : "N/A";

            String lowestSub = performance.getLowestScoringSubject() != null
                    ? String.format("%s (%.1f%%)", performance.getLowestScoringSubject().getSubject().getName(), performance.getLowestScoringSubject().getSubjectPercentage())
                    : "N/A";

            Paragraph insights = new Paragraph();
            insights.setFont(regularFont);
            insights.setSpacingAfter(18);
            insights.add(new Chunk("• Strongest Subject: ", boldFont));
            insights.add(new Chunk(highestSub + " — High mastery demonstrated.\n", regularFont));
            insights.add(new Chunk("• Weakest Subject:   ", boldFont));
            insights.add(new Chunk(lowestSub + " — Primary target for daily study revision.\n", regularFont));
            insights.add(new Chunk("• Mentor Evaluation: ", boldFont));
            insights.add(new Chunk(schedule.getSummaryAdvice() + "\n", regularFont));
            document.add(insights);

            // 8. RECOMMENDED PERSONALIZED STUDY TIMETABLE
            Paragraph section3 = new Paragraph("3. Recommended Daily Study Timetable (4.0 Hours Plan)", sectionHeadingFont);
            section3.setSpacingAfter(8);
            document.add(section3);

            PdfPTable schedTable = new PdfPTable(new float[]{1.2f, 3.5f, 2f, 2.2f, 6f});
            schedTable.setWidthPercentage(100);
            schedTable.setSpacingAfter(15);

            addHeaderCell(schedTable, "Slot", tableHeaderFont);
            addHeaderCell(schedTable, "Activity / Subject", tableHeaderFont);
            addHeaderCell(schedTable, "Priority", tableHeaderFont);
            addHeaderCell(schedTable, "Duration", tableHeaderFont);
            addHeaderCell(schedTable, "Actionable Focus Area", tableHeaderFont);

            int slotNum = 1;
            for (ScheduleSlot slot : schedule.getSlots()) {
                addBodyCell(schedTable, "#" + slotNum++, regularFont, Element.ALIGN_CENTER);
                addBodyCell(schedTable, slot.getSubjectName(), slot.isBreak() ? regularFont : boldFont, Element.ALIGN_LEFT);
                addBodyCell(schedTable, slot.getPriority(), boldFont, Element.ALIGN_CENTER);
                addBodyCell(schedTable, slot.getDurationMinutes() + " mins", regularFont, Element.ALIGN_CENTER);
                addBodyCell(schedTable, slot.getFocusArea(), regularFont, Element.ALIGN_LEFT);
            }
            document.add(schedTable);

            // 9. FOOTER NOTICE
            Paragraph footer = new Paragraph("Generated automatically by the Intelligent Daily Schedule Optimizer. Powered by Core Java & Spring Boot.",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, new Color(148, 163, 184)));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF report: " + e.getMessage(), e);
        }
    }

    private void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(PRIMARY_BLUE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        cell.setBorderColor(BORDER_GRAY);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6);
        cell.setBorderColor(BORDER_GRAY);
        table.addCell(cell);
    }

    private void addInfoCell(PdfPTable table, String label, String value, Font labelFont, Font valFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(LIGHT_BG);
        cell.setBorderColor(BORDER_GRAY);
        cell.setPadding(6);

        Paragraph p = new Paragraph();
        p.add(new Chunk(label + " ", labelFont));
        p.add(new Chunk(value, valFont));
        cell.addElement(p);

        table.addCell(cell);
    }
}
