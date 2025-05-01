package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.LoginDto;
import com.cg.crypto_wallet.DTO.ResetPasswordDto;
import com.cg.crypto_wallet.DTO.RegisterDto;
import com.cg.crypto_wallet.DTO.ResponseDto;
import com.cg.crypto_wallet.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody RegisterDto registerDTO) {
        ResponseDto response = userService.registerUser(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginUser(@RequestBody LoginDto loginDTO) {
        ResponseDto response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return userService.resetPassword(resetPasswordDto);
    }

    @PostMapping("/forget-Password")
    public ResponseEntity<?> forgetPassword(@RequestBody String email) {
        log.info("Forget Password");
        return userService.forgetPassword(email);
    }

}