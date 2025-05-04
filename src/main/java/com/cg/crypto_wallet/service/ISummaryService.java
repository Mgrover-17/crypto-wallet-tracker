package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.SummaryResponseDto;
import com.cg.crypto_wallet.model.User;
import org.springframework.http.ResponseEntity;

public interface ISummaryService {

//    public SummaryResponseDto calculateSummary();
    public SummaryResponseDto calculateSummary(User user);
}
