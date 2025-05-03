//package com.cg.crypto_wallet.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
//@Data
//@Entity
//@AllArgsConstructor
//@NoArgsConstructor
//public class CryptoHolding {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Column(nullable = false)
//    private String coinName;
//
//    @Column(nullable = false)
//    private String coinSymbol;
//
//    @Column(nullable = false)
//    private double units;
//
//    @Column(nullable = false)
//    private double purchasePrice;
//
//    private LocalDate purchaseDate;
//}
