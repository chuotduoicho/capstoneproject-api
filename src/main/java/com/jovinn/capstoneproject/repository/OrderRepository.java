package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
}
