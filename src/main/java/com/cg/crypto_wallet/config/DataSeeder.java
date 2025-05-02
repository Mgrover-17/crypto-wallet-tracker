package com.cg.crypto_wallet.config;

import com.cg.crypto_wallet.enums.Role;
import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@gmail.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setUsername("Admin User");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("admin@123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
                System.out.println("Admin user created successfully");
            } else {
                System.out.println(" Admin user already exists, skipping seeding.");
            }
        };
    }
}
