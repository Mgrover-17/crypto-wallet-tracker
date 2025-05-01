package com.cg.crypto_wallet.services;

import com.cg.crypto_wallet.model.Alert;

import java.util.List;

public class IAlertService {
    Alert createAlert(String email, String coinSymbol, double thresholdPrice, String alertType);
    List<Alert> getUserAlerts(String email);
    void evaluateAlerts(double currentPrice, String coinSymbol);
}
