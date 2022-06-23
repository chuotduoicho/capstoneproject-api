package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Seller findBySellerNumber(String sellerNumber);
    Optional<Seller> findSellerByUserId(UUID userId);
    Boolean existsByUserId(UUID userId);
    List<Seller> findAllById(UUID sellerId);
}
