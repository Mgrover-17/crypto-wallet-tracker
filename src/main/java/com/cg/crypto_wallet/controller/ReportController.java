//package com.cg.crypto_wallet.controller;
//
//import com.cg.crypto_wallet.DTO.SummaryResponseDto;
//import com.cg.crypto_wallet.service.IReportService;
//import com.cg.crypto_wallet.service.ISummaryService;
//import com.cg.crypto_wallet.service.ReportService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//
//@RestController
//@RequestMapping("/api/report")
//public class ReportController {
//
//    @Autowired
//    private ReportService reportService;
//
//    @GetMapping("/export")
//    public ResponseEntity<?> exportReport(@RequestParam("format") String format, Principal principal) {
//        if (format.equalsIgnoreCase("pdf")) {
//            byte[] pdfData = reportService.generatePdf(principal.getName());
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet-summary.pdf")
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .body(pdfData);
//        } else if (format.equalsIgnoreCase("excel")) {
//            byte[] excelData = reportService.generateExcel(principal.getName());
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wallet-summary.xlsx")
//                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//                    .body(excelData);
//        } else {
//            return ResponseEntity.badRequest().body("Invalid format: must be 'pdf' or 'excel'");
//        }
//    }
//}
//
