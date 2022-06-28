package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, UUID> {
}
