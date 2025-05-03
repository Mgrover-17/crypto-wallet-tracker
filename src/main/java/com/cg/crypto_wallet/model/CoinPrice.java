package com.cg.crypto_wallet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinPrice {

    @Id
    private String symbol;

    private String name;

    private double price;

    private LocalDateTime lastUpdatedTime;

}
