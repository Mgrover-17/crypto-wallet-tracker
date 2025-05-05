package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.exceptions.CryptoWalletException;
import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.AlertRepository;
import com.cg.crypto_wallet.repository.CoinPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AlertServiceImpl implements IAlertService {

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private CoinPriceRepository coinPriceRepository;

    @Autowired
    private EmailService emailService; // Service to handle email notifications

    @Override
    public Alert createAlert(Alert alert) {
        if (alert.getThreshold() <= 0) {
            throw new CryptoWalletException("Threshold value must be greater than 0");
        }
        // Save the new alert in the database
        return alertRepository.save(alert);
    }

    @Override
    public List<Alert> getActiveAlerts() {
        log.info("Fetching all active alerts");
        // Fetch all active alerts from the repository
        return alertRepository.findByActiveTrue();
    }

    @Override
    public List<Alert> getActiveAlertsForUser(User user) {
        log.info("Fetching active alerts for user: {}", user.getEmail());
        // Fetch active alerts for a specific user
        return alertRepository.findByUserAndActiveTrue(user);
    }


    @Override
    public Alert updateAlert(Long alertId, Alert updatedAlert, User loggedInUser) {
        Alert existingAlert = alertRepository.findById(alertId)
                .orElseThrow(() -> new CryptoWalletException("Alert not found with ID: " + alertId));

        if (!existingAlert.getUser().getId().equals(loggedInUser.getId())) {
            throw new CryptoWalletException("You are not authorized to update this alert");
        }

        // Update fields
        existingAlert.setCoinName(updatedAlert.getCoinName());
        existingAlert.setCoinSymbol(updatedAlert.getCoinSymbol());
        existingAlert.setThreshold(updatedAlert.getThreshold());
        existingAlert.setOperator(updatedAlert.getOperator());
        existingAlert.setActive(updatedAlert.isActive());

        return alertRepository.save(existingAlert);
    }

    private void sendEmailNotification(Alert alert,double currentPrice) {
        // Send an email notification to the user about the triggered alert
        // Assuming the user email is accessible from the Alert object
        emailService.sendAlertNotification(alert,currentPrice);
    }
}
