package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;

import java.util.List;

public interface IAlertService {

    // Create a new alert
    Alert createAlert(Alert alert);

    // Get all active alerts
    List<Alert> getActiveAlerts();

    // Get active alerts for a specific user
    List<Alert> getActiveAlertsForUser(User user);

    // Update an alert
    Alert updateAlert(Long alertId, Alert updatedAlert, User loggedInUser);

}
