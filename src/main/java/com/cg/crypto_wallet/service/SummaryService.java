
package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.CoinSummaryDto;
import com.cg.crypto_wallet.DTO.SummaryResponseDto;
import com.cg.crypto_wallet.model.CoinPrice;
import com.cg.crypto_wallet.model.CryptoHoldings;
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
        log.info("Calculating summary for user: " + user.getId());

        List<CryptoHoldings> holdings = holdingRepository.findByUser(user);
        List<CoinSummaryDto> coinSummaries = new ArrayList<>();

        double totalBuyValue = 0;
        double totalCurrentValue = 0;

        for (CryptoHoldings holding : holdings) {
            String coin = holding.getCoinSymbol();
            double units = holding.getUnits();
            double buyPrice = holding.getPurchasePrice();

            CoinPrice price = coinPriceRepository.findById(coin).orElse(null);
            if (price == null) {
                log.error("CoinPrice not found for coin: " + coin + " for user: " + user.getId());
                continue;
            }

            double currentPrice = price.getPrice();
            double totalBuy = units * buyPrice;
            double currentValue = units * currentPrice;
            double gainLoss = currentValue - totalBuy;
            double gainLossPercent = totalBuy == 0 ? 0 : (gainLoss / totalBuy) * 100;

            log.info("Processing coin: " + coin + " | Buy Value: " + totalBuy + " | Current Value: " + currentValue +
                    " | Gain/Loss: " + gainLoss + " | Gain/Loss Percent: " + gainLossPercent);

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
