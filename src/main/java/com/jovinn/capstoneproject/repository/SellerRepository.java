package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.EnumType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Seller findBySellerNumber(String sellerNumber);
    Optional<Seller> findSellerByUserId(UUID userId);
    List<Seller> findAllById(UUID sellerId);
    @Query(value = "SELECT * FROM jovinn_server.seller order by (case rank_seller when 'PROFESSIONAL' then 1 when 'ADVANCED' then 2 when 'BEGINNER' then 3 else 100 end) ASC, total_order_finish desc limit 3", nativeQuery = true)
    List<Seller> findTop3ByRankSeller();
}
