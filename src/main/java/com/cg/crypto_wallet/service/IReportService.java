package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.SummaryResponseDto;

public interface IReportService {
    byte[] generateExcel(String username);

    byte[] generatePdf(String username);
}
