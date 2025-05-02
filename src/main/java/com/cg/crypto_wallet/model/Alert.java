package com.cg.crypto_wallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Table(name = "alerts") // Optional: Customize the table name
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="coin_name", nullable = false)
    private String coinName;

    @Column(name = "coin_symbol", nullable = false)  // Custom column name
    private String coinSymbol; // The symbol of the coin (e.g., BTC, ETH)

    @Column(nullable = false)
    private double threshold; // The price threshold

    @Column(nullable = false)
    private String operator; // Comparison operator (e.g., ">", "<")

    @Column(nullable = false)
    private boolean active = true; // Active flag for the alert

    @ManyToOne
    @JoinColumn(name = "user_id") // Foreign key reference to User table
    private User user; // Reference to the user who created the alert
}
