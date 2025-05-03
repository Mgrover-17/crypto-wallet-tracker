package com.cg.crypto_wallet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "crypto_holdings")
public class CryptoHolding1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coin name is required")
    private String coinName;

    @NotBlank(message = "Coin symbol is required")
    private String coinSymbol;

    @Positive(message = "Units must be positive")
    private double units;

    @Positive
    private Double purchasePrice;

    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
