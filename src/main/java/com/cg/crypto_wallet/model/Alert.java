package com.cg.crypto_wallet.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coin name cannot be blank") // Ensures coin name is not empty or null
    @Column(name="coin_name", nullable = false)
    private String coinName;

    @NotBlank(message = "Coin symbol cannot be blank") // Ensures coin symbol is not empty or null
    @Column(name = "coin_symbol", nullable = false)
    private String coinSymbol;

    @Min(value = 1, message = "Threshold must be greater than 0") // Ensures threshold is greater than 0
    @Column(nullable = false)
    private double threshold;

    @Pattern(regexp = ">|<|>=|<=|==", message = "Operator must be one of: >, <, >=, <=, ==") // Validates the operator
    @Column(nullable = false)
    private String operator;

    @Column(nullable = false)
    private boolean active = true;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull(message = "User must be provided") // Ensures user is not null
    private User user;
}
