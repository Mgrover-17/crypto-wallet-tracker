package com.cg.crypto_wallet.repository;

import com.cg.crypto_wallet.model.CryptoHolding;
import com.cg.crypto_wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CryptoHoldingsRepository extends JpaRepository<CryptoHolding, Integer> {
    List<CryptoHolding> findByUser(User user);
}
