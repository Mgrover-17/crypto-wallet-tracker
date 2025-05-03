package com.cg.crypto_wallet.service;

import com.cg.crypto_wallet.DTO.*;
import com.cg.crypto_wallet.enums.Role;
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
        ResponseDto res = new ResponseDto("Registration","Initiating User Registration..." );
        if (existsByEmail(registerDTO.getEmail())) {
            log.warn("registration failed: user already exists with email {}", registerDTO.getEmail());
            res.setMessage("User Already exist with these credentials");
            return res;
        }
        User user = new User();
        user.setName(registerDTO.getFullname());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        log.info("user {} registered successfully", user.getEmail());
        res.setMessage("You have successfully registered to Crypto Wallet Application");
        res.setData(user);
        return res;

    }


    @Override
    public ResponseDto loginUser(LoginDto loginDTO) {
        log.info("Login attempt for user: {}", loginDTO.getEmail());
        ResponseDto res = new ResponseDto("Login", "Initiating Login process...");
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
                emailService.sendEmail(user.getEmail(), "Logged in Crypto-Wallet Application. You have been successfully logged in", token);log.error("User not found with email: {}", loginDTO.getEmail());
                res.setMessage("User has Successfully logged in here is the token generated:- ");
                res.setData(token);
                return res;
            } else {
                log.warn("Invalid credentials for user: {}", loginDTO.getEmail());
                res.setMessage("Wrong Credentials");
                res.setData("Pls re-fill your details");
                return res;
            }
        }
        res.setMessage("No data found");
        res.setData("User does not exist in Database");
        return res;
    }
    public boolean matchPassword(String rawPassword, String encodedPassword) {
        log.debug("Matching password for login attempt");
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

   public ResponseDto deleteUserById(Long id){
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            ResponseDto res = new ResponseDto("Not Found","User not present in Database");
            return res;
        }
        userRepository.delete(user);
        return new ResponseDto("Deletion","User Deleted Successfully");
    }

    @Override
    public ResponseDto changePassword(ChangePasswordDto changePasswordDto) {
        log.info("Change password attempt for user: {}", changePasswordDto.getEmail());
        ResponseDto res = new ResponseDto("Change Password","Intiating the Password Changes");
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

                res.setMessage("Change Password.");
                res.setData("User Changed the password Successfully");
                return res;
            } else {
                log.warn("Old password mismatch for user: {}", changePasswordDto.getEmail());
                res.setMessage("error Old password is incorrect.");
                res.setData("Old password does not match with one present in Database");
                return res;
            }
        }

        log.error("User not found with email: {}", changePasswordDto.getEmail());
        res.setMessage("No such Credentials");
        res.setData("User not found with theses Credentials in Database");
        return res;
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPasswordDto) {
        String email = resetPasswordDto.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("User not found.", "No such user in Database"));
        }

        // verify OTP
        if (!otp.equals(resetPasswordDto.getOtp())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Mismatch otp", "OTP doesn't match"));
        }

        // check new password and confirm password match
        if (!resetPasswordDto.getNewPassword().equals(resetPasswordDto.getConfirmPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("Password matching fault", "Confirm Password doesn't match."));
        }

        // encode new password and save
        String encodedPassword = passwordEncoder.encode(resetPasswordDto.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // reset OTP
        otp = "";

        return ResponseEntity.ok(new ResponseDto("New password updated successfully", user));
    }
    @Override
    public ResponseEntity<?> forgetPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            log.warn("User not found with email: {}", email);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("User not found.", "No such User present in Database"));
        }

        otp = otpGenerator.generateOTP();
        emailService.sendEmail(email, "OTP Generated", "OTP to reset password is: " + otp);

        return ResponseEntity.ok(new ResponseDto("OTP Generated", otp));
    }


    //updateUser
    @Override
    public ResponseDto updateUserDetails(Long id, UpdateUserDto updateUserDto) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User user1 = user.get();
            user1.setName(updateUserDto.getName());
            user1.setEmail(updateUserDto.getEmail());
            userRepository.save(user1);
        }
        return new ResponseDto("User updated successfully", "Updated the user Data in Database");
    }


}