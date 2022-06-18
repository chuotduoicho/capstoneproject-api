package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BoxRepository extends JpaRepository<Box, UUID> {
    List<Box> findAllById(UUID sellerId);
}
