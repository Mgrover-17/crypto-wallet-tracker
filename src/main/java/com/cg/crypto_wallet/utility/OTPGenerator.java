package com.cg.crypto_wallet.utility;

import org.springframework.stereotype.Component;

@Component
public class OTPGenerator {
    public String generateOTP() {
        int otp = 100000 + (int) (Math.random() * 900000);
        return String.valueOf(otp);
    }
}
