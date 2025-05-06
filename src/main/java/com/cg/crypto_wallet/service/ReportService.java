package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.CoinSummaryDto;
import com.cg.crypto_wallet.DTO.SummaryResponseDto;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReportService {

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public byte[] generatePdf(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        SummaryResponseDto summary = summaryService.calculateSummary(user);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Crypto Wallet Summary Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Table
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{1.5f, 1f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});

            // Headers
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            BaseColor headerColor = new BaseColor(63, 81, 181); // Indigo
            String[] headers = {"Coin", "Units", "Buy Price", "Total Buy", "Current Price", "Current Value", "Gain/Loss", "Gain/Loss %"};
            for (String h : headers) {
                PdfPCell header = new PdfPCell(new Phrase(h, headerFont));
                header.setBackgroundColor(headerColor);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setPadding(5);
                table.addCell(header);
            }

            // Data rows
            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
            for (CoinSummaryDto dto : summary.getCoins()) {
                table.addCell(new PdfPCell(new Phrase(dto.getCoin(), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getUnits()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getBuyPricePerUnit()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getTotalBuyValue()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getCurrentPrice()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getCurrentValue()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getGainLoss()), dataFont)));
                table.addCell(new PdfPCell(new Phrase(String.format("%.2f%%", dto.getGainLossPercent()), dataFont)));
            }

            document.add(table);

            // Summary section
            Font summaryFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.BLACK);
            Paragraph summaryTitle = new Paragraph("Overall Summary", summaryFont);
            summaryTitle.setSpacingBefore(10);
            document.add(summaryTitle);

            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            document.add(new Paragraph("Total Buy Value: $" + summary.getTotalBuyValue(), valueFont));
            document.add(new Paragraph("Total Current Value: $" + summary.getTotalCurrentValue(), valueFont));
            document.add(new Paragraph("Total Gain/Loss: $" + summary.getTotalGainLoss(), valueFont));
            document.add(new Paragraph(String.format("Total Gain/Loss %%: %.2f%%", summary.getTotalGainLossPercent()), valueFont));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    public byte[] generateExcel(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        SummaryResponseDto summary = summaryService.calculateSummary(user);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Crypto Wallet Report");

            // Header row
            Row header = sheet.createRow(0);
            String[] columns = {"Coin", "Units", "Buy Price", "Total Buy", "Current Price", "Current Value", "Gain/Loss", "Gain/Loss %"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            // Data rows
            int rowIdx = 1;
            for (CoinSummaryDto dto : summary.getCoins()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(dto.getCoin());
                row.createCell(1).setCellValue(dto.getUnits());
                row.createCell(2).setCellValue(dto.getBuyPricePerUnit());
                row.createCell(3).setCellValue(dto.getTotalBuyValue());
                row.createCell(4).setCellValue(dto.getCurrentPrice());
                row.createCell(5).setCellValue(dto.getCurrentValue());
                row.createCell(6).setCellValue(dto.getGainLoss());
                row.createCell(7).setCellValue(dto.getGainLossPercent());
            }

            // Summary Section
            Row summaryTitleRow = sheet.createRow(rowIdx++);
            summaryTitleRow.createCell(0).setCellValue("Overall Summary");

            Row totalBuyRow = sheet.createRow(rowIdx++);
            totalBuyRow.createCell(0).setCellValue("Total Buy Value");
            totalBuyRow.createCell(1).setCellValue(summary.getTotalBuyValue());

            Row totalCurrentRow = sheet.createRow(rowIdx++);
            totalCurrentRow.createCell(0).setCellValue("Total Current Value");
            totalCurrentRow.createCell(1).setCellValue(summary.getTotalCurrentValue());

            Row gainLossRow = sheet.createRow(rowIdx++);
            gainLossRow.createCell(0).setCellValue("Total Gain/Loss");
            gainLossRow.createCell(1).setCellValue(summary.getTotalGainLoss());

            Row gainLossPercentRow = sheet.createRow(rowIdx++);
            gainLossPercentRow.createCell(0).setCellValue("Total Gain/Loss %");
            gainLossPercentRow.createCell(1).setCellValue(summary.getTotalGainLossPercent());

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Excel generation failed", e);
        }
    }
}
//package com.cg.crypto_wallet.service;
//
//import com.cg.crypto_wallet.DTO.CoinSummaryDto;
//import com.cg.crypto_wallet.DTO.SummaryResponseDto;
//import com.cg.crypto_wallet.model.User;
//import com.cg.crypto_wallet.repository.UserRepository;
//import com.itextpdf.text.*;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.stream.Stream;
//
//@Service
//public class ReportService {
//
//    @Autowired
//    private SummaryService summaryService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private EmailService emailService;
//
//    public byte[] generatePdf(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow();
//        SummaryResponseDto summary = summaryService.calculateSummary(user);
//
//        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            Document document = new Document(PageSize.A4);
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            // Title styling
//            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
//            Paragraph title = new Paragraph("Crypto Wallet Summary Report", titleFont);
//            title.setAlignment(Element.ALIGN_CENTER);
//            document.add(title);
//            document.add(Chunk.NEWLINE);
//
//            // Table with 8 columns
//            PdfPTable table = new PdfPTable(8);
//            table.setWidthPercentage(100);
//            table.setSpacingBefore(10f);
//            table.setSpacingAfter(10f);
//            table.setWidths(new float[]{1.5f, 1f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f, 1.5f});
//
//            // Header styling
//            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
//            BaseColor headerColor = new BaseColor(63, 81, 181); // Indigo
//
//            String[] headers = {"Coin", "Units", "Buy Price", "Total Buy", "Current Price", "Current Value", "Gain/Loss", "Gain/Loss %"};
//            for (String h : headers) {
//                PdfPCell header = new PdfPCell(new Phrase(h, headerFont));
//                header.setBackgroundColor(headerColor);
//                header.setHorizontalAlignment(Element.ALIGN_CENTER);
//                header.setPadding(5);
//                table.addCell(header);
//            }
//
//            // Data rows
//            Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9, BaseColor.BLACK);
//            for (CoinSummaryDto dto : summary.getCoins()) {
//                table.addCell(new PdfPCell(new Phrase(dto.getCoin(), dataFont)));
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getUnits()), dataFont)));
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getBuyPricePerUnit()), dataFont)));
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getTotalBuyValue()), dataFont)));
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getCurrentPrice()), dataFont)));
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getCurrentValue()), dataFont)));
//                table.addCell(new PdfPCell(new Phrase(String.valueOf(dto.getGainLoss()), dataFont)));
//                table.addCell(new PdfPCell(new Phrase(String.format("%.2f%%", dto.getGainLossPercent()), dataFont)));
//            }
//
//            document.add(table);
//            document.close();
//            return out.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("PDF generation failed", e);
//        }
//    }
//
//
//    public byte[] generateExcel(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow();
//
//        SummaryResponseDto summary = summaryService.calculateSummary(user);
//
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Crypto Wallet Report");
//
//            Row header = sheet.createRow(0);
//            String[] columns = {"Coin", "Units", "Buy Price", "Total Buy", "Current Price", "Current Value", "Gain/Loss", "Gain/Loss %"};
//            for (int i = 0; i < columns.length; i++) {
//                header.createCell(i).setCellValue(columns[i]);
//            }
//
//            int rowIdx = 1;
//            for (CoinSummaryDto dto : summary.getCoins()) {
//                Row row = sheet.createRow(rowIdx++);
//                row.createCell(0).setCellValue(dto.getCoin());
//                row.createCell(1).setCellValue(dto.getUnits());
//                row.createCell(2).setCellValue(dto.getBuyPricePerUnit());
//                row.createCell(3).setCellValue(dto.getTotalBuyValue());
//                row.createCell(4).setCellValue(dto.getCurrentPrice());
//                row.createCell(5).setCellValue(dto.getCurrentValue());
//                row.createCell(6).setCellValue(dto.getGainLoss());
//                row.createCell(7).setCellValue(dto.getGainLossPercent());
//            }
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            workbook.write(out);
//            return out.toByteArray();
//
//        } catch (IOException e) {
//            throw new RuntimeException("Excel generation failed", e);
//        }
//    }
//}
//
