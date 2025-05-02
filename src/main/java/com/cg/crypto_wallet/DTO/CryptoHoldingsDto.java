package com.cg.crypto_wallet.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CryptoHoldingsDto {
    private Long id;

    @NotBlank(message = "Coin name must not be blank")
    private String coinName;

    @NotBlank(message = "Coin symbol must not be blank")
    private String coinSymbol;

    @Positive(message = "Units must be greater than 0")
    private double units;

    @Positive(message = "Purchase price must be zero or positive")
    private double purchasePrice;

    @NotNull(message = "Purchase date must not be null")
    private LocalDate purchaseDate;
}
