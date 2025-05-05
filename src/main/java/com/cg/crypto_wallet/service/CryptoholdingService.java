package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.CryptoHoldingsDto;
import com.cg.crypto_wallet.exceptions.CryptoWalletException;
import com.cg.crypto_wallet.exceptions.ResourceNotFoundException;
import com.cg.crypto_wallet.model.CryptoHoldings;
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
        log.info("Adding new holding for user: {}", email);

        if (dto.getUnits() <= 0) {
            throw new CryptoWalletException("Units must be greater than 0");
        }

        CryptoHoldings holding = new CryptoHoldings();
        holding.setCoinName(dto.getCoinName());
        holding.setCoinSymbol(dto.getCoinSymbol());
        holding.setUnits(dto.getUnits());
        holding.setPurchasePrice(dto.getPurchasePrice());
        holding.setPurchaseDate(dto.getPurchaseDate());
        holding.setUser(user);

        CryptoHoldings saved = repository.save(holding);
        dto.setCryptoId(saved.getCryptoId());
        return dto;
    }

    @Override
    public List<CryptoHoldingsDto> getHoldings(String email) {
        User user = getUserByEmail(email);
        log.info("Fetching holdings for user: {}", email);

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
        log.info("Updating holding ID: {} for user: {}", id, email);

        CryptoHoldings holding = repository.findById(id)
                .filter(h -> h.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found or access denied"));

        holding.setCoinName(dto.getCoinName());
        holding.setCoinSymbol(dto.getCoinSymbol());
        holding.setUnits(dto.getUnits());
        holding.setPurchasePrice(dto.getPurchasePrice());
        holding.setPurchaseDate(dto.getPurchaseDate());

        repository.save(holding);
        dto.setCryptoId(holding.getCryptoId());
        log.debug("Updated holding: {}", holding);
        return dto;
    }

    @Override
    public void deleteHolding(String email, Long id) {
        User user = getUserByEmail(email);
        log.info("Attempting to delete holding ID: {} for user: {}", id, email);

        CryptoHoldings holding = repository.findById(id)
                .filter(h -> h.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found or access denied"));

        log.debug("Checking if user ID {} matches holding user ID {}", user.getId(), holding.getUser().getId());

        repository.delete(holding);
        log.info("Deleted holding: {}", id);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }
}
