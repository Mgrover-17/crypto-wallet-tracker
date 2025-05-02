package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.model.MockCoinPrice;
import org.springframework.stereotype.Service;

@Service
public class MockCoinPriceService  {

    // This is just a mock method to simulate fetching coin prices.
    public MockCoinPrice getPrice(String coinSymbol) {
        // For example, returning static data for BTC and ETH
        MockCoinPrice coinPrice = new MockCoinPrice();
        if ("BTC".equals(coinSymbol)) {
            coinPrice.setCoinSymbol("BTC");
            coinPrice.setPrice(45001.00);  // Static price for BTC
        } else if ("ETH".equals(coinSymbol)) {
            coinPrice.setCoinSymbol("ETH");
            coinPrice.setPrice(45002.00);  // Static price for ETH
        } else {
            coinPrice.setCoinSymbol(coinSymbol);
            coinPrice.setPrice(0.0);  // Default for unknown coin symbols
        }
        return coinPrice;
    }
}
