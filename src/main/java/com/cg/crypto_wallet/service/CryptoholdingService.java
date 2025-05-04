package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.CryptoHoldingsDto;
import com.cg.crypto_wallet.model.CryptoHolding1;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.CryptoHoldingsRepository;
import com.cg.crypto_wallet.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CryptoholdingService implements ICryptoholdingService{

    @Autowired
    private CryptoHoldingsRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CryptoHoldingsDto addHolding(String email, CryptoHoldingsDto dto) {
        User user = getUserByEmail(email);

        CryptoHolding1 holding = new CryptoHolding1();
        holding.setCoinName(dto.getCoinName());
        holding.setCoinSymbol(dto.getCoinSymbol());
        holding.setUnits(dto.getUnits());
        holding.setPurchasePrice(dto.getPurchasePrice());
        holding.setPurchaseDate(dto.getPurchaseDate());
        holding.setUser(user);

        CryptoHolding1 saved = repository.save(holding);
        dto.setCryptoId(saved.getCryptoId());
        return dto;
    }

    @Override
    public List<CryptoHoldingsDto> getHoldings(String email) {
        User user = getUserByEmail(email);

        return repository.findByUser(user).stream()
                .map(holding -> {
                    CryptoHoldingsDto dto = new CryptoHoldingsDto();
                    dto.setCryptoId(holding.getCryptoId());
                    dto.setCoinName(holding.getCoinName());
                    dto.setCoinSymbol(holding.getCoinSymbol());
                    dto.setUnits(holding.getUnits());
                    dto.setPurchasePrice(holding.getPurchasePrice());
                    dto.setPurchaseDate(holding.getPurchaseDate());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CryptoHoldingsDto updateHolding(String email, Long id, CryptoHoldingsDto dto) {
        User user = getUserByEmail(email);

        CryptoHolding1 holding = repository.findById(id)
                .filter(h -> h.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Holding not found or access denied"));

        holding.setCoinName(dto.getCoinName());
        holding.setCoinSymbol(dto.getCoinSymbol());
        holding.setUnits(dto.getUnits());
        holding.setPurchasePrice(dto.getPurchasePrice());
        holding.setPurchaseDate(dto.getPurchaseDate());

        repository.save(holding);
        dto.setCryptoId(holding.getCryptoId());
        return dto;
    }

    @Override
    public void deleteHolding(String email, Long id) {
        User user = getUserByEmail(email);

        CryptoHolding1 holding = repository.findById(id)
                .filter(h -> h.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Holding not found or access denied"));

        repository.delete(holding);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}
