package com.cg.crypto_wallet.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CryptoHolding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String coinSymbol; // e.g., "BTC"

    @Column(precision = 18, scale = 8) // For crypto units (supports decimals)
    private double units;

    @Column(precision = 18, scale = 2) // For USD price
    private double buyPrice;

    private LocalDateTime purchaseDate;
}
