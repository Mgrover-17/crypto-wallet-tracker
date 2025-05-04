package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import com.cg.crypto_wallet.service.AlertServiceImpl;
import com.cg.crypto_wallet.service.EmailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/alerts")  // Base URL for the alerts
public class AlertController {

    @Autowired
    private AlertServiceImpl alertService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    // Create a new alert
//    @PostMapping("/create-alert")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Alert createAlert(@Valid @RequestBody Alert alert) {
//        log.info("Creating alert " + alert);
//        return alertService.createAlert(alert);
//    }
    @PostMapping("/create-alert")
    public ResponseEntity<Alert> createAlert(@RequestBody Alert alert, Principal principal) {
        String userEmail = principal.getName(); // Get logged-in user email
        log.info("logged in email " + userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        alert.setUser(user); // Link alert to the logged-in user
        Alert createdAlert = alertService.createAlert(alert);
        log.info("Alert created");
        return new ResponseEntity<>(createdAlert, HttpStatus.CREATED);
    }

//     Get active alerts for a user
    @GetMapping("/user/{userId}")
    public List<Alert> getActiveAlertsForUser(@PathVariable Long userId) {
        User user = new User(); // You will need to fetch the user from the database
        user.setId(userId);
        return alertService.getActiveAlertsForUser(user);
    }

//    @GetMapping("/user/{userId}")
//    public List<Alert> getActiveAlertsForUser(@PathVariable Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
//        log.info("found user: " + user);
//        return alertService.getActiveAlertsForUser(user);
//    }

//    @GetMapping("/my-alerts")
//    public List<Alert> getMyActiveAlerts(Principal principal) {
//
//        User userFromDb = userRepository.findByEmail(principal.getName())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        log.info("hello");
//        log.info("Found user: " + userFromDb);
//
//        User user = new User(); // You will need to fetch the user from the database
//        user.setId(userFromDb.getId());
//
//        return alertService.getActiveAlertsForUser(user);
//    }

    // Get all active alerts
    @GetMapping("/active")
    public List<Alert> getActiveAlerts() {
        return alertService.getActiveAlerts();
    }

    // Update an alert
//    @PutMapping("/{alertId}")
//    public Alert updateAlert(@PathVariable Long alertId, @RequestBody Alert updatedAlert) {
//        return alertService.updateAlert(alertId, updatedAlert);
//    }

    @PutMapping("/{alertId}")
    public Alert updateAlert(@PathVariable Long alertId, @RequestBody Alert updatedAlert, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Logged in user: ");

        return alertService.updateAlert(alertId, updatedAlert, user); // updated method
    }
}
