package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.ExtraOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExtraOfferRepository extends JpaRepository<ExtraOffer, UUID> {
}
