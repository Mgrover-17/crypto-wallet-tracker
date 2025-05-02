package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.MockCoinPrice;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlertServiceImpl implements IAlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private MockCoinPriceService mockCoinPriceService; // Service to get the latest coin prices

    @Autowired
    private EmailService emailService; // Service to handle email notifications

    @Override
    public Alert createAlert(Alert alert) {
        // Save the new alert in the database
        return alertRepository.save(alert);
    }

    @Override
    public List<Alert> getActiveAlerts() {
        // Fetch all active alerts from the repository
        return alertRepository.findByActiveTrue();
    }

    @Override
    public List<Alert> getActiveAlertsForUser(User user) {
        // Fetch active alerts for a specific user
        return alertRepository.findByUserAndActiveTrue(user);
    }

    @Override
    public Alert updateAlert(Long alertId, Alert updatedAlert) {
        // Find the existing alert by ID
        Alert existingAlert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        // Update the alert fields
        existingAlert.setCoinName(updatedAlert.getCoinName());
        existingAlert.setCoinSymbol(updatedAlert.getCoinSymbol());
        existingAlert.setThreshold(updatedAlert.getThreshold());
        existingAlert.setOperator(updatedAlert.getOperator());
        existingAlert.setActive(updatedAlert.isActive());

        // Save the updated alert in the database
        return alertRepository.save(existingAlert);
    }


    @Override
    @Transactional
    public void evaluateAlerts() {
        // Fetch all active alerts
        List<Alert> activeAlerts = alertRepository.findByActiveTrue();

        // Loop through each alert and evaluate if the condition is met
        for (Alert alert : activeAlerts) {
            // Fetch the current price for the coin
            MockCoinPrice mockCoinPrice=mockCoinPriceService.getPrice(alert.getCoinSymbol());

            // Evaluate if the alert condition is met (based on operator)
            if (evaluateCondition(alert, mockCoinPrice)) {
                // Send email notification to the user
                sendEmailNotification(alert, mockCoinPrice.getPrice());
                alert.setActive(false);
                alertRepository.save(alert);
            }
        }
    }

    private boolean evaluateCondition(Alert alert, MockCoinPrice mockCoinPrice) {
        double currentPrice = mockCoinPrice.getPrice();

        // Check the condition based on the alert operator (e.g., >, <)
        switch (alert.getOperator()) {
            case ">":
                return currentPrice > alert.getThreshold();
            case "<":
                return currentPrice < alert.getThreshold();
            default:
                return false;
        }
    }

    private void sendEmailNotification(Alert alert,double currentPrice) {
        // Send an email notification to the user about the triggered alert
        // Assuming the user email is accessible from the Alert object
        emailService.sendAlertNotification(alert,currentPrice);
    }
}
