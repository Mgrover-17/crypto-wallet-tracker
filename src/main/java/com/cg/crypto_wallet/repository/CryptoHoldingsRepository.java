package com.cg.crypto_wallet.repository;

//import com.cg.crypto_wallet.model.CryptoHolding;
import com.cg.crypto_wallet.model.CryptoHolding1;
import com.cg.crypto_wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CryptoHoldingsRepository extends JpaRepository<CryptoHolding1, Long> {
    List<CryptoHolding1> findByUser(User user);

    // Query to fetch distinct coin names from the CryptoHoldings table
    @Query("SELECT DISTINCT ch.coinName FROM CryptoHolding1 ch")
    List<String> findDistinctCoinNames();
}
