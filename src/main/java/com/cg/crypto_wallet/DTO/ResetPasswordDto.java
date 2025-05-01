package com.cg.crypto_wallet.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordDto {
    private String email;
    private String otp;
    private String newPassword;
    private String confirmPassword;
}
