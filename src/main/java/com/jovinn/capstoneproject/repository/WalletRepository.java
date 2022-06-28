package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    Wallet findWalletByUserId(UUID userId);
}
