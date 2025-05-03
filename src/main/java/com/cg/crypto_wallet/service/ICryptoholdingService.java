package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.CryptoHoldingsDto;

import java.util.List;

public interface ICryptoholdingService {

    CryptoHoldingsDto addHolding(String email, CryptoHoldingsDto dto);
    List<CryptoHoldingsDto> getHoldings(String email);
    CryptoHoldingsDto updateHolding(String email, Long id, CryptoHoldingsDto dto);
    void deleteHolding(String email, Long id);
}
