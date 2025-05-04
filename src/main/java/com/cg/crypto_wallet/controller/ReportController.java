package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.SummaryResponseDto;
import com.cg.crypto_wallet.service.EmailService;
import com.cg.crypto_wallet.service.IReportService;
import com.cg.crypto_wallet.service.ISummaryService;
import com.cg.crypto_wallet.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private EmailService emailService;

//    @GetMapping("/export")
//    public ResponseEntity<?> exportReport(@RequestParam("format") String format, Principal principal) {
//
//        try{
//
//            if (format.equalsIgnoreCase("pdf")) {
//                byte[] pdfData = reportService.generatePdf(principal.getName());
//
//    //            send via email
//                emailService.sendAttachment(principal.getName(), pdfData, "wallet-summary.pdf", "application/pdf");
//
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet-summary.pdf")
//                        .contentType(MediaType.APPLICATION_PDF)
//                        .body(pdfData);
//            } else if (format.equalsIgnoreCase("excel")) {
//                byte[] excelData = reportService.generateExcel(principal.getName());
//
//                emailService.sendAttachment(principal.getName(), excelData, "wallet-summary.xlsx",
//                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet-summary.xlsx")
//                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//                        .body(excelData);
//            } else {
//                return ResponseEntity.badRequest().body("Invalid format: must be 'pdf' or 'excel'");
//            }
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate or send report.");
//        }
//    }

    @GetMapping("/export")
    public ResponseEntity<?> exportReport(@RequestParam("format") String format, Principal principal) {
        String email = principal.getName();

        System.out.println("📨 Requested report format: " + format);
        System.out.println("🔐 Logged in user email (from Principal): " + email);

        try {
            if (format.equalsIgnoreCase("pdf")) {
                byte[] pdfData = reportService.generatePdf(email);

                System.out.println("✅ PDF report generated successfully");

                emailService.sendAttachment(email.trim(), pdfData, "wallet-summary.pdf", "application/pdf");
                System.out.println("📧 PDF report sent to user email: " + email);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet-summary.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(pdfData);

            } else if (format.equalsIgnoreCase("excel")) {
                byte[] excelData = reportService.generateExcel(email);

                System.out.println("✅ Excel report generated successfully");

                emailService.sendAttachment(email.trim(), excelData, "wallet-summary.xlsx",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                System.out.println("📧 Excel report sent to user email: " + email);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet-summary.xlsx")
                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .body(excelData);

            } else {
                System.out.println("❌ Invalid format requested: " + format);
                return ResponseEntity.badRequest().body("Invalid format: must be 'pdf' or 'excel'");
            }
        } catch (Exception e) {
            System.out.println("❌ Error occurred: " + e.getMessage());
            e.printStackTrace();  // for stack trace
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate or send report.");
        }
    }


}

