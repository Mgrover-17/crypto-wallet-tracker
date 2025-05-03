package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.service.AlertServiceImpl;
import com.cg.crypto_wallet.service.EmailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/alerts")  // Base URL for the alerts
public class AlertController {

    @Autowired
    private AlertServiceImpl alertService;

    @Autowired
    private EmailService emailService;

    // Create a new alert
    @PostMapping("/create-alert")
    @ResponseStatus(HttpStatus.CREATED)
    public Alert createAlert(@Valid @RequestBody Alert alert) {
        log.info("Creating alert " + alert);
        return alertService.createAlert(alert);
    }

    // Get active alerts for a user
    @GetMapping("/user/{userId}")
    public List<Alert> getActiveAlertsForUser(@PathVariable Long userId) {
        User user = new User(); // You will need to fetch the user from the database
        user.setId(userId);
        return alertService.getActiveAlertsForUser(user);
    }

    // Get all active alerts
    @GetMapping("/active")
    public List<Alert> getActiveAlerts() {
        return alertService.getActiveAlerts();
    }

    // Update an alert
    @PutMapping("/{alertId}")
    public Alert updateAlert(@PathVariable Long alertId, @RequestBody Alert updatedAlert) {
        return alertService.updateAlert(alertId, updatedAlert);
    }
}
