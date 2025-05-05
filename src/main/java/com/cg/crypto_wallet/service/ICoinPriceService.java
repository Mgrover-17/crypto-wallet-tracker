package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.model.CoinPrice;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ICoinPriceService {

    public void fetchAndUpdatePrices();

    public Optional<CoinPrice> getPrice(String symbol);
}
