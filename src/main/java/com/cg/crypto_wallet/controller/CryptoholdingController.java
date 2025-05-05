package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.CryptoHoldingsDto;
import com.cg.crypto_wallet.service.CryptoholdingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/wallet")
public class CryptoholdingController {

    @Autowired
    private CryptoholdingService service;

    //  Add a new holding
    @PostMapping
    public ResponseEntity<CryptoHoldingsDto> addHolding(@RequestBody CryptoHoldingsDto dto, Principal principal) {
        log.info("Hello starting point ");
        String email = principal.getName();
        log.info(email);
        return ResponseEntity.ok(service.addHolding(email, dto));
    }

    // Get all holdings for logged-in user
    @GetMapping
    public ResponseEntity<List<CryptoHoldingsDto>> getHoldings(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(service.getHoldings(email));
    }

    //  Update a holding
    @PutMapping("/{id}")
    public ResponseEntity<CryptoHoldingsDto> updateHolding(
            @PathVariable Long id,
            @RequestBody CryptoHoldingsDto dto,
            Principal principal) {
        String email = principal.getName();
        log.info("PUT /api/wallet/{} called by: {}", id, email);
        return ResponseEntity.ok(service.updateHolding(email, id, dto));
    }

    //  Delete a holding
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHolding(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        service.deleteHolding(email, id);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
    }
}
