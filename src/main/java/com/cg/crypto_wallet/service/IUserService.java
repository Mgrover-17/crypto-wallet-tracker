package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.*;
import org.springframework.http.ResponseEntity;

public interface IUserService {
    public ResponseDto loginUser(LoginDto loginDTO);
    public ResponseDto registerUser(RegisterDto registerDTO);
    public ResponseDto deleteUserById(Long id);
    ResponseDto changePassword(ChangePasswordDto changePasswordDto);
    ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto);
    ResponseEntity<?> forgetPassword(String email);
    public ResponseDto updateUserDetails(Long id, UpdateUserDto updateUserDto);
}
