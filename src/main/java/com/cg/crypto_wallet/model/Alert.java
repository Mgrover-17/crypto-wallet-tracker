package com.cg.crypto_wallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Reference to the user who set the alert

    private String coinSymbol;
    private Double thresholdPrice;
    private boolean active; // Whether the alert is active or not

    private String alertType; // Can be "greaterThan" or "lessThan" or other conditions
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String email;
}
