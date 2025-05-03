package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.*;
import com.cg.crypto_wallet.service.IUserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ResponseDto> registerUser(@Valid @RequestBody RegisterDto registerDTO) {
        ResponseDto response = userService.registerUser(registerDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginUser( @Valid @RequestBody LoginDto loginDTO) {
        ResponseDto response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/deleteUser/{id}")
    public ResponseDto deleteUser(@Valid @PathVariable Long id) {
        return userService.deleteUserById(id);

    }
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        return userService.resetPassword(resetPasswordDto);
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody String email) {
        log.info("Forget Password");
        return userService.forgetPassword(email);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<ResponseDto> changePassword( @Valid @RequestBody ChangePasswordDto changePasswordDto) {
        ResponseDto response = userService.changePassword(changePasswordDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<ResponseDto> updateUserDetails(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDto updateUserDto) {

        log.info("Updating user details for ID: {}", id);
        ResponseDto response = userService.updateUserDetails(id, updateUserDto);
        return ResponseEntity.ok(response);
    }


}