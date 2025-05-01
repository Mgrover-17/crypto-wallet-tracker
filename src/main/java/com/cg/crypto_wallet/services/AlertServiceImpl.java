package com.cg.crypto_wallet.services;


import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.repositories.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertServiceImpl implements IAlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public Alert createAlert(String email, String coinSymbol, double thresholdPrice, String alertType) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        Alert alert = new Alert();
        alert.setUser(user);
        alert.setCoinSymbol(coinSymbol);
        alert.setThresholdPrice(thresholdPrice);
        alert.setAlertType(alertType);
        alert.setActive(true);
        alert.setEmail(email);
        alert.setCreatedAt(LocalDateTime.now());
        alert.setUpdatedAt(LocalDateTime.now());

        return alertRepository.save(alert);
    }

    @Override
    public List<Alert> getUserAlerts(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return alertRepository.findByUser(user);
    }

    @Override
    public void evaluateAlerts(double currentPrice, String coinSymbol) {
        List<Alert> alerts = alertRepository.findByCoinSymbolAndActiveTrue(coinSymbol);

        for (Alert alert : alerts) {
            boolean shouldTrigger = false;

            if ("greaterThan".equalsIgnoreCase(alert.getAlertType()) && currentPrice > alert.getThresholdPrice()) {
                shouldTrigger = true;
            } else if ("lessThan".equalsIgnoreCase(alert.getAlertType()) && currentPrice < alert.getThresholdPrice()) {
                shouldTrigger = true;
            }

            if (shouldTrigger) {
                emailService.sendAlertEmail(alert.getEmail(), coinSymbol, currentPrice, alert.getThresholdPrice());
                alert.setActive(false);
                alert.setUpdatedAt(LocalDateTime.now());
                alertRepository.save(alert);
            }
        }
    }
}
