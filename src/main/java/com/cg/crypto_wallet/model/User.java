package com.cg.crypto_wallet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String token;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // No roles for now
    }
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<CryptoHolding> holdings;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Alert> alerts;
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Transaction> transactions;

//    private LocalDateTime createdAt;
}