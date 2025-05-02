package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.CryptoHoldingsDto;
import com.cg.crypto_wallet.service.CryptoHoldingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class CryptoHoldingsController {

    @Autowired
    private CryptoHoldingsService holdingService;

    private String getEmail(Authentication authentication) {
        return (authentication != null) ? authentication.getName() : "yagya3108@gmail.com";
    }

    @PostMapping
    public ResponseEntity<CryptoHoldingsDto> addHolding(@RequestBody CryptoHoldingsDto dto, Authentication authentication) {
        String email = getEmail(authentication);
        return ResponseEntity.ok(holdingService.addHolding(email, dto));
    }

    @GetMapping
    public ResponseEntity<List<CryptoHoldingsDto>> getAllHoldings(Authentication authentication) {
        String email = getEmail(authentication);
        return ResponseEntity.ok(holdingService.getHoldings(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CryptoHoldingsDto> updateHoldings(@PathVariable int id,
                                                    @RequestBody CryptoHoldingsDto dto,
                                                    Authentication authentication) {
        String email = getEmail(authentication);
        return ResponseEntity.ok(holdingService.updateHolding(email, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHoldings(@PathVariable int id, Authentication authentication) {
        String email = getEmail(authentication);
        holdingService.deleteHolding(email, id);
        return ResponseEntity.ok().build();
    }
}
