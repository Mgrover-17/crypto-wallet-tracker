package com.cg.crypto_wallet.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryResponseDto {

    private List<CoinSummaryDto> coins;
    private double totalBuyValue;
    private double totalCurrentValue;
    private double totalGainLoss;
    private double totalGainLossPercent;

}
