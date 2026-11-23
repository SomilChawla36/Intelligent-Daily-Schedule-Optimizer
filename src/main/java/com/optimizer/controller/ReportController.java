package com.optimizer.controller;

import com.optimizer.service.PdfReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for generating and exporting student academic PDF report cards.
 * Base Path: /api/reports
 * Demonstrates:
 * - Returning binary data (application/pdf) from a REST endpoint
 * - Configuring HTTP Content-Disposition headers for browser downloads
 */
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final PdfReportService pdfReportService;

    public ReportController(PdfReportService pdfReportService) {
        this.pdfReportService = pdfReportService;
    }

    /**
     * GET /api/reports/download/{rollNumber}
     * Generates and prompts an immediate download of the student's PDF report card.
     */
    @GetMapping("/download/{rollNumber}")
    public ResponseEntity<byte[]> downloadReport(@PathVariable String rollNumber) {
        byte[] pdfBytes = pdfReportService.generatePerformanceReportPdf(rollNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ReportCard_" + rollNumber + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * GET /api/reports/view/{rollNumber}
     * Previews the PDF directly inside the browser's PDF viewer.
     */
    @GetMapping("/view/{rollNumber}")
    public ResponseEntity<byte[]> previewReport(@PathVariable String rollNumber) {
        byte[] pdfBytes = pdfReportService.generatePerformanceReportPdf(rollNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=ReportCard_" + rollNumber + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
