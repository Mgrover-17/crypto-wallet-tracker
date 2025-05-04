package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.CoinPrice;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.AlertRepository;
import com.cg.crypto_wallet.repository.CoinPriceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CryptoWalletReportService {

//    @Autowired
//    private final WalletRepository walletRepository;

    @Autowired
    private final AlertRepository alertRepository;

    @Autowired
    private final CoinPriceRepository coinPriceRepository;

    @Autowired
    private EmailService emailService;

    public byte[] generateMonthlyReport(User user, YearMonth month) throws Exception {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

//        List<Wallet> wallets = walletRepository.findByUser(user);
//        List<Alert> triggeredAlerts = alertRepository.findByUserAndTriggeredAtBetween(user, start, end);

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font header = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font subHeader = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normal = new Font(Font.FontFamily.HELVETICA, 12);

        document.add(new Paragraph("Crypto Wallet Tracker Report", header));
        document.add(new Paragraph("User: " + user.getName(), normal));
        document.add(new Paragraph("Month: " + month, normal));
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Portfolio Overview", subHeader));
        // document.add(generateWalletTable(wallets));
        document.add(generateWalletTable());
        document.add(Chunk.NEWLINE);

//        document.add(new Paragraph("Triggered Alerts", subHeader));
//        document.add(generateAlertTable(triggeredAlerts));
//        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("Note", subHeader));
        document.add(new Paragraph("This report summarizes the wallet and alert activity for the selected month.", normal));

        document.close();
        return out.toByteArray();
    }


    //private PdfPTable generateWalletTable(List<Wallet> wallets) throws DocumentException {
    private PdfPTable generateWalletTable() throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 2, 2, 2, 2});

        table.addCell("Coin");
        table.addCell("Quantity");
        table.addCell("Price");
        table.addCell("Value");
        table.addCell("Last Updated");

        BigDecimal totalValue = BigDecimal.ZERO;

//        for (Wallet wallet : wallets) {
        for(int i=0;i<5;i++){
//            CoinPrice coinPrice = coinPriceRepository.findLatestBySymbol(wallet.getCoinSymbol());
//
//            BigDecimal quantity = wallet.getQuantity();
//            BigDecimal price = coinPrice.getPrice();
//            BigDecimal value = quantity.multiply(price);
//
//            totalValue = totalValue.add(value);

//            table.addCell(wallet.getCoinSymbol());
//            table.addCell(quantity.toPlainString());
//            table.addCell("₹" + price.toPlainString());
//            table.addCell("₹" + value.toPlainString());
//            table.addCell(coinPrice.getUpdatedAt().toString());

            table.addCell("BTC");
            table.addCell("10");
            table.addCell("₹" + "50");
            table.addCell("₹" +"60");
            table.addCell("1 hr hogya");
        }

        PdfPCell totalCell = new PdfPCell(new Phrase("Total Portfolio Value"));
        totalCell.setColspan(4);
        totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(totalCell);
        table.addCell("₹" + totalValue.toPlainString());
        return table;
    }

//    private PdfPTable generateAlertTable(List<Alert> alerts) throws DocumentException {
//        PdfPTable table = new PdfPTable(4);
//        table.setWidthPercentage(100);
//        table.setWidths(new int[]{2, 2, 3, 3});
//
//        table.addCell("Coin");
//        table.addCell("Condition");
//        table.addCell("Triggered Price");
//        table.addCell("Triggered At");
//
//        if (alerts.isEmpty()) {
//            PdfPCell noData = new PdfPCell(new Phrase("No alerts triggered this month"));
//            noData.setColspan(4);
//            noData.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(noData);
//        } else {
//            for (Alert alert : alerts) {
//                table.addCell(alert.getCoinSymbol());
//                table.addCell(alert.getCondition());
//                table.addCell("₹" + alert.getTriggeredPrice().toPlainString());
//                table.addCell(alert.getTriggeredAt().toString());
//            }
//        }
//
//        return table;
//    }
}
