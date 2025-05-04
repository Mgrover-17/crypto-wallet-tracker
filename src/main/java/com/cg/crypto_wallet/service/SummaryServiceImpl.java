//package com.cg.crypto_wallet.service;
//
//import com.cg.crypto_wallet.DTO.CoinSummaryDto;
//import com.cg.crypto_wallet.DTO.SummaryResponseDto;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SummaryServiceImpl {
//
//    public SummaryResponseDto calculateSummary() {
//        List<CoinSummaryDto> coinSummaries = new ArrayList<>();
//
//        // Dummy data: coin, units, buy price, current price
//        coinSummaries.add(build("BTC", 0.5, 60000, 65000));
//        coinSummaries.add(build("ETH", 2.0, 3000, 2950));
//        coinSummaries.add(build("SOL", 10.0, 100, 120));
//
//        double totalBuyValue = 0;
//        double totalCurrentValue = 0;
//
//        for (CoinSummaryDto c : coinSummaries) {
//            totalBuyValue += c.getTotalBuyValue();
//            totalCurrentValue += c.getCurrentValue();
//        }
//
//        double totalGainLoss = totalCurrentValue - totalBuyValue;
//        double totalGainLossPercent = (totalGainLoss / totalBuyValue) * 100;
//
//        return new SummaryResponseDto(coinSummaries, totalBuyValue, totalCurrentValue, totalGainLoss, totalGainLossPercent);
//    }
//
//    private CoinSummaryDto build(String coin, double units, double buyPrice, double currentPrice) {
//        double totalBuyValue = units * buyPrice;
//        double currentValue = units * currentPrice;
//        double gainLoss = currentValue - totalBuyValue;
//        double gainLossPercent = (gainLoss / totalBuyValue) * 100;
//
//        return new CoinSummaryDto(coin, units, buyPrice, totalBuyValue, currentPrice, currentValue, gainLoss, gainLossPercent);
//    }
//}
//
//
//
//}