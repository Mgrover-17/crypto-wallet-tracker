package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.*;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import com.cg.crypto_wallet.utility.JwtUtility;
import com.cg.crypto_wallet.utility.OTPGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPGenerator otpGenerator;

    private String otp;

    public boolean existsByEmail(String email) {
        log.debug("Checking existence of user by email: {}", email);
        return userRepository.findByEmail(email).isPresent();
    }

    public Optional<User> getUserByEmail(String email) {
        log.debug("Fetching user by email: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public ResponseDto registerUser(RegisterDto registerDTO) {
        log.info("Registering user:{}", registerDTO.getEmail());
        ResponseDto res = new ResponseDto("", HttpStatus.OK);
        if (existsByEmail(registerDTO.getEmail())) {
            log.warn("registration failed: user already exists with email {}", registerDTO.getEmail());
            res.setMessage("error");
            return res;
        }
        User user = new User();
        user.setUsername(registerDTO.getFullname());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("user {} registered successfully", user.getEmail());
        res.setMessage("message");
        return res;

    }


    @Override
    public ResponseDto loginUser(LoginDto loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getEmail());
        ResponseDto res = new ResponseDto("", HttpStatus.OK);
        Optional<User> userExists = getUserByEmail(loginDTO.getEmail());

        if (userExists.isPresent()) {
            User user = userExists.get();
            if (matchPassword(loginDTO.getPassword(), user.getPassword())) {
                String token = jwtUtility.generateToken(user.getEmail());
                log.debug("Generated token for user {}: {}", user.getEmail(), token);
                System.out.println("Generated JWT token: " + token);
                user.setToken(token);
                userRepository.save(user);

                log.debug("Login successful for user: {} - Token generated", user.getEmail());
                emailService.sendEmail(user.getEmail(), "Logged in Crypto-Wallet Application. You have been successfully logged in", token);
                log.error("User not found with email: {}", loginDTO.getEmail());
                res.setMessage("message " + token);
                return res;
            } else {
                log.warn("Invalid credentials for user: {}", loginDTO.getEmail());
                res.setMessage("error");
                return res;
            }
        }
        res.setMessage("error");
        return res;
    }
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        log.debug("Matching password for login attempt");
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

   public ResponseDto deleteUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            ResponseDto res = new ResponseDto("User not found", HttpStatus.NOT_FOUND);
            return res;
        }
        userRepository.delete(user);
        return new ResponseDto("User Deleted Successfully", HttpStatus.OK);
    }

    @Override
    public ResponseDto changePassword(ChangePasswordDto changePasswordDto) {
        log.info("Change password attempt for user: {}", changePasswordDto.getEmail());
        ResponseDto res = new ResponseDto("", HttpStatus.OK);
        Optional<User> userExists = getUserByEmail(changePasswordDto.getEmail());

        if (userExists.isPresent()) {
            User user = userExists.get();

            if (matchPassword(changePasswordDto.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                userRepository.save(user);
                log.info("Password changed successfully for user: {}", user.getEmail());

                emailService.sendEmail(
                        user.getEmail(),
                        "Crypto-Wallet: Password Changed",
                        "Your password has been successfully changed."
                );

                res.setMessage("message Password changed successfully.");
                return res;
            } else {
                log.warn("Old password mismatch for user: {}", changePasswordDto.getEmail());
                res.setMessage("error Old password is incorrect.");
                return res;
            }
        }

        log.error("User not found with email: {}", changePasswordDto.getEmail());
        res.setMessage("error User not found.");
        return res;
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        String email = resetPasswordDto.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);
        //verify otp
        if(!otp.equals(resetPasswordDto.getOtp())){
            return new ResponseEntity<>(new ResponseDto("OTP doesn't match.", null), HttpStatus.BAD_REQUEST);
        }
        //new password and confirm password match
        if(!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())){
            return new ResponseEntity<>(new ResponseDto("Confirm Password doesn't match.", null), HttpStatus.BAD_REQUEST);
        }

        //new password ko encode kra and save kra
        String encodedPassword=passwordEncoder.encode(resetPasswordDto.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        //otp ko reset krrhe h
        otp="";

        return new ResponseEntity<>(new ForgetResetResponse("New password updated successfully", user), HttpStatus.OK);

    }


    @Override
    public ResponseEntity<?> forgetPassword(String email) {
        User user=userRepository.findByEmail(email).orElse(null);
        if(user==null){
            log.warn("User not found with email: {}", email);
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }
        otp= otpGenerator.generateOTP();
        emailService.sendEmail(email, "OTP Generated", "OTP to reset password is: "+ otp);

        return new ResponseEntity<>(new ForgetResetResponse("OTP Generated", otp), HttpStatus.OK);

    }
    
}