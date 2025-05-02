package com.cg.crypto_wallet.utility;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.repository.AlertRepository;
import com.cg.crypto_wallet.model.MockCoinPrice;

import com.cg.crypto_wallet.service.EmailService;
import com.cg.crypto_wallet.service.MockCoinPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertCheckerJob {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private MockCoinPriceService mockCoinPriceService;

    @Autowired
    private EmailService emailService;

    // This will run every 1 minute (60000 ms)
    @Scheduled(cron = "0 0/1 * *  * ?" )
    public void checkAlerts() {
        List<Alert> activeAlerts = alertRepository.findByActiveTrue();

        for (Alert alert : activeAlerts) {

            // Yaha API Call hoggii jaha sa current price niklega
             MockCoinPrice currentPrice = mockCoinPriceService.getPrice(alert.getCoinSymbol());

            boolean shouldTrigger = false;

            switch (alert.getOperator()) {
                case ">":
                    shouldTrigger = currentPrice.getPrice() > alert.getThreshold();
                    break;
                case "<":
                    shouldTrigger = currentPrice.getPrice() < alert.getThreshold();
                    break;
                case ">=":
                    shouldTrigger = currentPrice.getPrice() >= alert.getThreshold();
                    break;
                case "<=":
                    shouldTrigger = currentPrice.getPrice() <= alert.getThreshold();
                    break;
                case "==":
                    shouldTrigger = currentPrice.getPrice() == alert.getThreshold();
                    break;
                default:
                    System.out.println("Invalid operator: " + alert.getOperator());
            }

            if (shouldTrigger) {
                emailService.sendAlertNotification(alert,currentPrice.getPrice());

                // Yaha Delete krdena Alert ko instead of non active
//                alert.setActive(false); // deactivate after firing
//                alertRepository.save(alert);

                alertRepository.delete(alert);
                System.out.println("Alert triggered for " + alert.getCoinSymbol() + " and deactivated.");
            }
        }
    }
}
