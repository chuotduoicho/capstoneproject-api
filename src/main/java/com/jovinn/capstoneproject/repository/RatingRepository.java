package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    List<Rating> findAllBySellerId(UUID sellerId);
    List<Rating> findAllByContractId(UUID contractId);
}
