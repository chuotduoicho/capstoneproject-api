package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.OfferRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface OfferRequestRepository extends JpaRepository<OfferRequest, UUID> {
    List<OfferRequest> findAllByPostRequestId(UUID postRequestId);
}
