package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Seller findBySellerNumber(String sellerNumber);
    List<Seller> findAllById(UUID sellerId);
}
