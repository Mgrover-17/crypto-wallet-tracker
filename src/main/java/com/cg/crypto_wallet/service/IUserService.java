package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.LoginDto;
import com.cg.crypto_wallet.DTO.ResetPasswordDto;
import com.cg.crypto_wallet.DTO.RegisterDto;
import com.cg.crypto_wallet.DTO.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    public ResponseDto loginUser(LoginDto loginDTO);
    public ResponseDto registerUser(RegisterDto registerDTO);
    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto);
    ResponseEntity<?> forgetPassword(String email);
}
