package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.SummaryResponseDto;
import com.cg.crypto_wallet.service.SummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    @Autowired
    private SummaryService summaryService;

    @GetMapping
    public ResponseEntity<SummaryResponseDto> getSummary(){
        SummaryResponseDto response = summaryService.calculateSummary();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
