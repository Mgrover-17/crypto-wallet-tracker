package com.cg.crypto_wallet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MockCoinPrice {
    private String coinSymbol;
    private double price;
}
