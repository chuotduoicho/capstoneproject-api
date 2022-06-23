package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EnumType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Seller findBySellerNumber(String sellerNumber);
    Optional<Seller> findSellerByUserId(UUID userId);
    List<Seller> findAllById(UUID sellerId);

    List<Seller> findTop3ByRankSeller(RankSeller Rank);
}
