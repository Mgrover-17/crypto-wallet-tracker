//package com.cg.crypto_wallet.controller;
//
//import com.cg.crypto_wallet.DTO.CryptoHoldingsDto;
//import com.cg.crypto_wallet.service.CryptoHoldingsService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Slf4j
//@RestController
//@RequestMapping("/api/wallet")
//public class CryptoHoldingsController {
//
//    @Autowired
//    private CryptoHoldingsService holdingService;
//
////    private String getEmail(Authentication authentication) {
////        return (authentication != null) ? authentication.getName() : "yagya3108@gmail.com";
////        return (authentication != null) ? authentication.getName() : "nikhil0948.be21@chitkara.edu.in";
////    }
//
//    @PostMapping
//    public ResponseEntity<CryptoHoldingsDto> addHolding(@RequestBody CryptoHoldingsDto dto, Authentication authentication) {
////        String email = getEmail(authentication);
//        String email = authentication.getName();
//        log.info(email);
//        return ResponseEntity.ok(holdingService.addHolding(email, dto));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<CryptoHoldingsDto>> getAllHoldings(Authentication authentication) {
////        String email = getEmail(authentication);
//        String email = authentication.getName();
//        return ResponseEntity.ok(holdingService.getHoldings(email));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<CryptoHoldingsDto> updateHoldings(@PathVariable int id,
//                                                    @RequestBody CryptoHoldingsDto dto,
//                                                    Authentication authentication) {
////        String email = getEmail(authentication);
//        String email = authentication.getName();
//        return ResponseEntity.ok(holdingService.updateHolding(email, id, dto));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteHoldings(@PathVariable int id, Authentication authentication) {
////        String email = getEmail(authentication);
//        String email = authentication.getName();
//        holdingService.deleteHolding(email, id);
////        return ResponseEntity.ok().build();
//        return ResponseEntity.ok("Successfully Deleted. ");
//    }
//}
