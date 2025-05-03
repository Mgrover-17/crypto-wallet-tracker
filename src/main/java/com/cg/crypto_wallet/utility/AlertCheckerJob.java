package com.cg.crypto_wallet.utility;

import com.cg.crypto_wallet.exceptions.CryptoWalletException;
import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.CoinPrice;
import com.cg.crypto_wallet.repository.AlertRepository;

import com.cg.crypto_wallet.repository.CoinPriceRepository;
import com.cg.crypto_wallet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertCheckerJob {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CoinPriceRepository coinPriceRepository;


    // This will run every 1 minute (60000 ms)
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkAlerts() {
        List<Alert> activeAlerts = alertRepository.findByActiveTrue();

        for (Alert alert : activeAlerts) {
            try {
                CoinPrice currentPrice = coinPriceRepository.getCoinPricesBySymbol(alert.getCoinSymbol());

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
                        throw new CryptoWalletException("Invalid operator: " + alert.getOperator());
                }

                if (shouldTrigger) {
                    emailService.sendAlertNotification(alert, currentPrice.getPrice());
                    alertRepository.delete(alert);
                    System.out.println("Alert triggered for " + alert.getCoinSymbol() + " and deactivated.");
                }

            } catch (Exception e) {
                System.err.println("Error while processing alert ID: " + alert.getId() + " — " + e.getMessage());
                e.printStackTrace(); // Optionally log using a logger
            }
        }
    }
}