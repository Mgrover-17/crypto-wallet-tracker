//package com.cg.crypto_wallet.service;
//
//import com.cg.crypto_wallet.DTO.CryptoHoldingsDto;
//import com.cg.crypto_wallet.model.CryptoHolding;
//import com.cg.crypto_wallet.model.User;
//import com.cg.crypto_wallet.repository.CryptoHoldingsRepository;
//import com.cg.crypto_wallet.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class CryptoHoldingsService implements ICryptoHoldingsService {
//
//    @Autowired
//    private CryptoHoldingsRepository cryptoHoldingsRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public CryptoHoldingsDto addHolding(String userEmail, CryptoHoldingsDto dto) {
//        User user = getUserByEmail(userEmail);
//        CryptoHolding holding = mapToEntity(dto);
//        holding.setUser(user);
//        return mapToDTO(cryptoHoldingsRepository.save(holding));
//    }
//
//    @Override
//    public List<CryptoHoldingsDto> getHoldings(String userEmail) {
//        User user = getUserByEmail(userEmail);
//        return cryptoHoldingsRepository.findByUser(user).stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public CryptoHoldingsDto updateHolding(String userEmail, int id, CryptoHoldingsDto dto) {
//        User user = getUserByEmail(userEmail);
//        CryptoHolding holding = cryptoHoldingsRepository.findById(id)
//                .filter(h -> h.getUser().getId().equals(user.getId()))
//                .orElseThrow(() -> new RuntimeException("Holding not found or access denied"));
//
//        holding.setCoinName(dto.getCoinName());
//        holding.setCoinSymbol(dto.getCoinSymbol());
//        holding.setUnits(dto.getUnits());
//        holding.setPurchasePrice(dto.getPurchasePrice());
//        holding.setPurchaseDate(dto.getPurchaseDate());
//
//        return mapToDTO(cryptoHoldingsRepository.save(holding));
//    }
//
//    @Override
//    public void deleteHolding(String userEmail, int id) {
//        User user = getUserByEmail(userEmail);
//        CryptoHolding holding = cryptoHoldingsRepository.findById(id)
//                .filter(h -> h.getUser().getId().equals(user.getId()))
//                .orElseThrow(() -> new RuntimeException("Holding not found or access denied"));
//        cryptoHoldingsRepository.delete(holding);
//    }
//
//    private CryptoHoldingsDto mapToDTO(CryptoHolding holding) {
//        CryptoHoldingsDto dto = new CryptoHoldingsDto();
//        dto.setId(holding.getId());
//        dto.setCoinName(holding.getCoinName());
//        dto.setCoinSymbol(holding.getCoinSymbol());
//        dto.setUnits(holding.getUnits());
//        dto.setPurchasePrice(holding.getPurchasePrice());
//        dto.setPurchaseDate(holding.getPurchaseDate());
//        return dto;
//    }
//
//    private CryptoHolding mapToEntity(CryptoHoldingsDto dto) {
//        CryptoHolding holding = new CryptoHolding();
//        holding.setCoinName(dto.getCoinName());
//        holding.setCoinSymbol(dto.getCoinSymbol());
//        holding.setUnits(dto.getUnits());
//        holding.setPurchasePrice(dto.getPurchasePrice());
//        holding.setPurchaseDate(dto.getPurchaseDate());
//        return holding;
//    }
//
//    private User getUserByEmail(String email) {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//}
//
