package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.*;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    ResponseDto loginUser(LoginDto loginDTO);
    ResponseDto registerUser(RegisterDto registerDTO);
    ResponseDto deleteUserById(Long id);
    ResponseDto changePassword(ChangePasswordDto changePasswordDto);
    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto);
    ResponseEntity<?> forgetPassword(String email);
    ResponseDto updateUserDetails(Long id, UpdateUserDto updateUserDto);
}
