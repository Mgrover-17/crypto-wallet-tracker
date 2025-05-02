package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.SummaryResponseDto;
import org.springframework.http.ResponseEntity;

public interface ISummaryService {

    public SummaryResponseDto calculateSummary();
}
