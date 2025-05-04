package com.cg.crypto_wallet.utility;

import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import com.cg.crypto_wallet.service.CryptoWalletReportService;
import com.cg.crypto_wallet.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.time.YearMonth;

@Component
@Slf4j
public class GenrateMonthlyReportScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CryptoWalletReportService cryptoWalletReportService;

    //Generate Report for all the user at 1:00 AM on evert month
    @Scheduled(cron = "0 0 1 1 * *")
    public void generateMonthlyReport() throws Exception {

        List<User> allUsers = userRepository.findAll();
        YearMonth lastMonth = YearMonth.now().minusMonths(1);

        for(User user : allUsers) {
            try{
                log.info("Generating report for user email: {}", user.getEmail());

                byte[] report = cryptoWalletReportService.generateMonthlyReport(user, lastMonth);

                if (report != null && report.length > 0) {
                    String subject = "Your Credit Report Summary for " + lastMonth.getMonth() + " " + lastMonth.getYear();
                    String body = "Dear " + user.getName() + ",\n\nPlease find attached portfolio summary for " + lastMonth.getMonth() + " " + lastMonth.getYear() + ".\n\nRegards,\nCredit Metrics Team";

                    emailService.sendEmailWithAttachment(user.getEmail(), subject, body, report, "Wallet-Report_" + lastMonth +"_"+ user.getName()  + ".pdf");

                    log.info("Report sent to user email: {}", user.getEmail());
                } else {
                    log.warn("Empty report generated for user email: {}", user.getEmail());
                }

            }
            catch (Exception e){
                log.error("Failed to generate/send report for user: {}", user.getEmail(), e);
            }
        }

        log.info("Monthly Report generated for all users: " + allUsers.size());
    }
}
