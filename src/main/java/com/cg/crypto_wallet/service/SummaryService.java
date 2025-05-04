
package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.CoinSummaryDto;
import com.cg.crypto_wallet.DTO.SummaryResponseDto;
import com.cg.crypto_wallet.model.CoinPrice;
import com.cg.crypto_wallet.model.CryptoHolding1;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.CoinPriceRepository;
import com.cg.crypto_wallet.repository.CryptoHoldingsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SummaryService implements ISummaryService {

    @Autowired
    private CryptoHoldingsRepository holdingRepository;

    @Autowired
    private CoinPriceRepository coinPriceRepository;

    @Override
    public SummaryResponseDto calculateSummary(User user) {
        // Get holdings only for the logged-in user
        List<CryptoHolding1> holdings = holdingRepository.findByUser(user);
        List<CoinSummaryDto> coinSummaries = new ArrayList<>();

        double totalBuyValue = 0;
        double totalCurrentValue = 0;

        for (CryptoHolding1 holding : holdings) {
            String coin = holding.getCoinSymbol();
            double units = holding.getUnits();
            double buyPrice = holding.getPurchasePrice();

            CoinPrice price = coinPriceRepository.findById(coin).orElse(null);
            if (price == null) {
                log.error("CoinPrice not found for " + coin);
                continue;
            }

            double currentPrice = price.getPrice();
            double totalBuy = units * buyPrice;
            double currentValue = units * currentPrice;
            double gainLoss = currentValue - totalBuy;
            double gainLossPercent = totalBuy == 0 ? 0 : (gainLoss / totalBuy) * 100;

            CoinSummaryDto dto = new CoinSummaryDto(
                    coin, units, buyPrice, totalBuy,
                    currentPrice, currentValue, gainLoss, gainLossPercent
            );

            coinSummaries.add(dto);
            totalBuyValue += totalBuy;
            totalCurrentValue += currentValue;
        }

        double totalGainLoss = totalCurrentValue - totalBuyValue;
        double totalGainLossPercent = totalBuyValue == 0 ? 0 : (totalGainLoss / totalBuyValue) * 100;

        return new SummaryResponseDto(coinSummaries, totalBuyValue, totalCurrentValue, totalGainLoss, totalGainLossPercent);
    }
}
//
//import com.cg.crypto_wallet.DTO.CoinSummaryDto;
//import com.cg.crypto_wallet.DTO.SummaryResponseDto;
//import com.cg.crypto_wallet.model.CoinPrice;
//import com.cg.crypto_wallet.model.CryptoHolding1;
//import com.cg.crypto_wallet.repository.CoinPriceRepository;
//import com.cg.crypto_wallet.repository.CryptoHoldingsRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Service
//public class SummaryService implements ISummaryService{
//
//    @Autowired
//    private CryptoHoldingsRepository holdingRepository;
//
//    @Autowired
//    private CoinPriceRepository coinPriceRepository;
//
//    @Override
//    public SummaryResponseDto calculateSummary() {
//        // Get all user holdings
//        List<CryptoHolding1> holdings = holdingRepository.findAll();
//        List<CoinSummaryDto> coinSummaries = new ArrayList<>();
//
//        double totalBuyValue = 0;
//        double totalCurrentValue = 0;
//
//        // Loop through holdings and calculate coin-wise summary
//        for (CryptoHolding1 holding : holdings) {
//            String coin = holding.getCoinSymbol();  // Get coin symbol (e.g. BTC, ETH)
//            double units = holding.getUnits(); // Units of the coin held
//            double buyPrice = holding.getPurchasePrice(); // Buy price per unit
//
//            // Fetch the current price of the coin from CoinPrice repository
//            CoinPrice price = coinPriceRepository.findById(coin).orElse(null);
//            if (price == null) {
//                // If coin price not found, skip this coin
//                log.error("CoinPrice not found for " + coin );
//                continue;
//            }
//
//            double currentPrice = price.getPrice(); // Get current price from repo
//
//            // Calculate the total buy value and current value for the coin
//            double totalBuy = units * buyPrice;
//            double currentValue = units * currentPrice;
//            double gainLoss = currentValue - totalBuy;
//            double gainLossPercent = totalBuy == 0 ? 0 : (gainLoss / totalBuy) * 100; // Prevent division by zero
//
//            // Create a CoinSummaryDto for this coin
//            CoinSummaryDto dto = new CoinSummaryDto(
//                    coin, units, buyPrice, totalBuy,
//                    currentPrice, currentValue, gainLoss, gainLossPercent
//            );
//
//            coinSummaries.add(dto);  // Add the coin summary to the list
//
//            // Accumulate the total buy and current values for all coins
//            totalBuyValue += totalBuy;
//            totalCurrentValue += currentValue;
//        }
//
//        // Calculate the overall gains or losses and percentage
//        double totalGainLoss = totalCurrentValue - totalBuyValue;
//        double totalGainLossPercent = totalBuyValue == 0 ? 0 : (totalGainLoss / totalBuyValue) * 100;
//
//        // Return the summary response with the list of coin summaries and totals
//        return new SummaryResponseDto(coinSummaries, totalBuyValue, totalCurrentValue, totalGainLoss, totalGainLossPercent);
//    }
//}
