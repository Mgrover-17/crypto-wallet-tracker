package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.service.CryptoWalletReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class CryptoWalletReportController {

    @Autowired
    private final CryptoWalletReportService reportService;

    @GetMapping("/wallet-summary")
    public void downloadWalletReport(
            @AuthenticationPrincipal User user,
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month,
            HttpServletResponse response
    ) {
        try {
            byte[] pdfData = reportService.generateMonthlyReport(user, month);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Wallet_Summary_" + month + ".pdf");
            response.getOutputStream().write(pdfData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

