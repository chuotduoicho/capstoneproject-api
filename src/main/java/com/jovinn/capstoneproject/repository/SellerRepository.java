package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Boolean existsBySellerNumber(String sellerNumber);
    Optional<Seller> findSellerByUserId(UUID userId);
    Boolean existsByUserId(UUID userId);
    Seller findSellerByBrandName(String brandName);
    List<Seller> findAllById(UUID sellerId);
    @Query(value = "SELECT * FROM jovinn_server.seller order by (case rank_seller when 'PROFESSIONAL' then 1 when 'ADVANCED' then 2 when 'BEGINNER' then 3 else 100 end) ASC, total_order_finish desc limit 3", nativeQuery = true)
    List<Seller> findTop3ByRankSeller();

    List<Seller> findAllByPostRequests_Id(UUID postRequestId);

}
