//package com.cg.crypto_wallet.service;
//
//import com.cg.crypto_wallet.DTO.CoinSummaryDto;
//import com.cg.crypto_wallet.DTO.SummaryResponseDto;
//import com.cg.crypto_wallet.model.User;
//import com.cg.crypto_wallet.repository.UserRepository;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.ByteArrayOutputStream;
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
//    public byte[] generatePdf(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow();
//
//        SummaryResponseDto summary = summaryService.calculateSummaryForUser(user);
//
//        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            Document document = new Document();
//            PdfWriter.getInstance(document, out);
//            document.open();
//
//            document.add(new Paragraph("Crypto Wallet Report for " + user.getUsername()));
//            document.add(new Paragraph(" "));
//
//            PdfPTable table = new PdfPTable(8);
//            Stream.of("Coin", "Units", "Buy Price", "Total Buy", "Current Price", "Current Value", "Gain/Loss", "Gain/Loss %")
//                    .forEach(header -> table.addCell(new PdfPCell(new Phrase(header))));
//
//            for (CoinSummaryDto dto : summary.getCoins()) {
//                table.addCell(dto.getCoin());
//                table.addCell(String.valueOf(dto.getUnits()));
//                table.addCell(String.valueOf(dto.getBuyPricePerUnit()));
//                table.addCell(String.valueOf(dto.getTotalBuyValue()));
//                table.addCell(String.valueOf(dto.getCurrentPrice()));
//                table.addCell(String.valueOf(dto.getCurrentValue()));
//                table.addCell(String.valueOf(dto.getGainLoss()));
//                table.addCell(String.format("%.2f%%", dto.getGainLossPercent()));
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
//    public byte[] generateExcel(String username) {
//        User user = userRepository.findByUsername(username).orElseThrow();
//
//        SummaryResponseDto summary = summaryService.calculateSummaryForUser(user);
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
