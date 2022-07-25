package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    Boolean existsByMilestoneId(UUID milestoneId);
}
