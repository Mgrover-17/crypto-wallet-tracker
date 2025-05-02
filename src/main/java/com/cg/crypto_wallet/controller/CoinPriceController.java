package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.model.CoinPrice;
import com.cg.crypto_wallet.service.CoinPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/price")
public class CoinPriceController {

    @Autowired
    private CoinPriceService schedulerService;

    // Manual trigger to fetch and update prices
    @PostMapping("/fetch-latest")
    public ResponseEntity<String> fetchNow() {
        schedulerService.fetchAndUpdatePrices();
        return ResponseEntity.ok("Prices updated manually.");
    }

    // View price of a specific coin
    @GetMapping("/{id}")
    public ResponseEntity<CoinPrice> getPrice(@PathVariable String id) {
        return ResponseEntity.of(schedulerService.getPrice(id));
    }
}
