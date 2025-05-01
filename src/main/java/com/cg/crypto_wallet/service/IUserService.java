package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.LoginDto;
import com.cg.crypto_wallet.DTO.ResetPasswordDto;
import com.cg.crypto_wallet.DTO.RegisterDto;
import com.cg.crypto_wallet.DTO.ResponseDto;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    public ResponseDto loginUser(LoginDto loginDTO);
    public ResponseDto registerUser(RegisterDto registerDTO);

    public ResponseDto deleteUserById(Long id);
//    public void initiatePasswordReset(ForgotPasswordDto request);
//    public void verifyOtp(VerifyOtpDto request);
//    public void resetPassword(ResetPasswordDto request);
//    public void ChangePassword(ChangePasswordDto changePasswordDto);

    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto);
    ResponseEntity<?> forgetPassword(String email);
}
