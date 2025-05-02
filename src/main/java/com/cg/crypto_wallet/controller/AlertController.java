package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.service.AlertServiceImpl;
import com.cg.crypto_wallet.service.EmailService;
import com.cg.crypto_wallet.service.MockCoinPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")  // Base URL for the alerts
public class AlertController {

    @Autowired
    private AlertServiceImpl alertService;

    @Autowired
    private MockCoinPriceService mockCoinPriceService;

    @Autowired
    private EmailService emailService;

    // Create a new alert
    @PostMapping("/create-alert")
    @ResponseStatus(HttpStatus.CREATED)
    public Alert createAlert(@RequestBody Alert alert) {
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

    // Trigger alert evaluation (for testing purposes, can be scheduled)
    @PostMapping("/evaluate")
    public void evaluateAlerts() {
        alertService.evaluateAlerts();
    }

    // Get the current price of a coin (useful for testing purposes)
    @GetMapping("/coin-price/{coinSymbol}")
    public double getCoinPrice(@PathVariable String coinSymbol) {
        return mockCoinPriceService.getPrice(coinSymbol).getPrice();
    }
}
