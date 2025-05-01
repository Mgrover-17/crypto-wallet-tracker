package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.LoginDto;
import com.cg.crypto_wallet.DTO.RegisterDto;
import com.cg.crypto_wallet.DTO.ResponseDto;
import com.cg.crypto_wallet.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody RegisterDto registerDTO) {
        ResponseDto response = userService.registerUser(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginUser( @Valid @RequestBody LoginDto loginDTO) {
        ResponseDto response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }

}