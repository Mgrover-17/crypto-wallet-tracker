package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.model.CoinPrice;
import com.cg.crypto_wallet.repository.CoinPriceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CoinPriceService implements ICoinPriceService {

    @Autowired
    private CoinPriceRepository coinPriceRepository;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String API_KEY = "CG-yFZBzE34DfhhzWMT9KDN7RoG";
    private final List<String> coinIds = List.of("bitcoin", "ethereum");

    // 🕒 Every 1 minutes (uncomment when needed)
//     @Scheduled(cron = "0 */1 * * * *")
    public void fetchAndUpdatePrices() {
        for (String coinId : coinIds) {
            try {
                String url = "https://api.coingecko.com/api/v3/coins/" + coinId;

                HttpHeaders headers = new HttpHeaders();
                headers.set("accept", "application/json");
                headers.set("x-cg-demo-api-key", API_KEY);

                HttpEntity<Void> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    JsonNode root = mapper.readTree(response.getBody());

                    String name = root.get("name").asText();
                    String symbol = root.get("symbol").asText();
                    double price = root.get("market_data").get("current_price").get("usd").asDouble();

                    updateOrCreate(symbol, name, price);
                    log.info("Price updated: " + name + " " + symbol + " " + price);

                } else {
                    System.err.println("❌ Failed response for " + coinId + ": " + response.getStatusCode());
                }

            } catch (Exception e) {
                System.err.println("❌ Exception for coin " + coinId + ": " + e.getMessage());
            }
        }

        System.out.println("✅ Prices, names & symbols updated from /coins/{id} endpoint");
    }

    private void updateOrCreate(String symbol, String name, double price) {
        CoinPrice coin = new CoinPrice();
        coin.setSymbol(symbol);
        coin.setName(name);
        coin.setPrice(BigDecimal.valueOf(price));
        coin.setLastUpdatedTime(LocalDateTime.now());
        coinPriceRepository.save(coin);
    }

    public Optional<CoinPrice> getPrice(String symbol) {
        return coinPriceRepository.findById(symbol);
    }
}


