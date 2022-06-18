package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, UUID> {
    Buyer findByBuyerNumber(String buyerNumber);
}
