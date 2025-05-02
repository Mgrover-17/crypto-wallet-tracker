package com.cg.crypto_wallet.repository;

import com.cg.crypto_wallet.model.CoinPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinPriceRepository extends JpaRepository<CoinPrice, String> {

}
