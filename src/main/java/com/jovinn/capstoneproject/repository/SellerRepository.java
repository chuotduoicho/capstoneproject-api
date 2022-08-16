package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    Page<Seller> findAllByPostRequests_Id(UUID postRequestId, Pageable pageable);
    List<Seller> findAllByRankSeller(RankSeller rankSeller);
    @Query("SELECT s.id from Seller s " +
            "INNER JOIN Box b on b.seller.id = s.id " +
            "INNER JOIN Skill sk on sk.seller.id = s.id " +
            "where b.subCategory.id = :subCategoryId " +
            "AND s.rankSeller = :rankSeller " +
            "OR sk.name IN :idsSkillName " +
            "GROUP BY s.id " +
            "ORDER BY RAND()")
    List<String> getTenSellerBySubCategoryId(@Param("subCategoryId") UUID subCategoryId,
                                             @Param("rankSeller") RankSeller rankSeller,
                                             @Param("idsSkillName") Set<String> idsSkillName, PageRequest pageRequest);
}
