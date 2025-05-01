package com.cg.crypto_wallet.repository;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByUser(User user);
    List<Alert> findByCoinSymbolAndActiveTrue(String coinSymbol);
}
