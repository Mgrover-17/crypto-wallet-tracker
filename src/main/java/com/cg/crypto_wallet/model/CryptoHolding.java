package com.cg.crypto_wallet.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class CryptoHolding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String coinSymbol;

    @Column(nullable = false)
    private double units;

    @Column(nullable = false)
    private double purchasePrice;

    private LocalDateTime purchaseDate;
}
