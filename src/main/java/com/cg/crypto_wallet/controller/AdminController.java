package com.cg.crypto_wallet.controller;

import com.cg.crypto_wallet.DTO.ResponseDto;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("all user info");
        return ResponseEntity.ok(users);
    }

    // Delete user by ID
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            log.info("User not found with the Id");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("User not found with ID: " + id, "No such user in Database"));
        }

        userRepository.deleteById(id);
        log.info("User Deleted with the given id");
        return ResponseEntity.ok(new ResponseDto("User deleted successfully", "Deleted user from Database"));
    }
}
