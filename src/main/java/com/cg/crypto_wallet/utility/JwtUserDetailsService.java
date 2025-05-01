package com.cg.crypto_wallet.utility;

import com.cg.crypto_wallet.model.User;
import com.cg.crypto_wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+ email));
        return user;
    }
}
