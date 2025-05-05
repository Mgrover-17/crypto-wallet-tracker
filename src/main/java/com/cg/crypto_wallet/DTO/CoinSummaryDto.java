package com.cg.crypto_wallet.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinSummaryDto {

    private String coin;
    private double units;
    private double buyPricePerUnit;
    private double totalBuyValue;
    private double currentPrice;
    private double currentValue;
    private double gainLoss;
    private double gainLossPercent;
}
