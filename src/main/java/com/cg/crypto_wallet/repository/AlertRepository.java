package com.cg.crypto_wallet.repository;

import com.cg.crypto_wallet.model.Alert;
import com.cg.crypto_wallet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    // Custom query methods can be added here if needed
    List<Alert> findByUserAndActiveTrue(User user);  // Find active alerts for a user
    List<Alert> findByActiveTrue(); // Find all active alerts
}
