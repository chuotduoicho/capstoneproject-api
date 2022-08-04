package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.model.Box;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BoxRepository extends JpaRepository<Box, UUID> {
    List<Box> findAllBySellerId(UUID sellerId);

    @Query("select b from Box b where b.subCategory.category.id = :catId")
    List<Box> getAllServiceByCategoryId(@Param("catId") UUID catId);

    long countBySubCategory_Category_Id(UUID catId);

    Page<Box> findAllBySubCategory_Category_Id(UUID catId, PageRequest pageRequest);

    Page<Box> findAllBySubCategory_NameContainsOrSubCategory_Category_NameContains(String subCatName, String catName, PageRequest pageRequest);

    @Query("SELECT s.id from Seller s JOIN s.boxes b " +
            "INNER JOIN Skill sk on sk.seller.id = s.id " +
            "where b.subCategory.id = :subCategoryId " +
            "and s.rankSeller = :rankSeller " +
            "and sk.name IN :idsSkillName " +
            "GROUP BY s.id " +
            "ORDER BY RAND()")
    List<String> getTenSellerBySubCategoryId(@Param("subCategoryId") UUID subCategoryId,
                                          @Param("rankSeller") RankSeller rankSeller,
                                          @Param("idsSkillName") Set<String> idsSkillName, PageRequest pageRequest);
//    @Query("select b from Box b where b.serviceType.subCategory.category.name = :catName or b.serviceType.name = :serviceTypeName or b.packages.")
//    List<Box> searchAllServiceByCatNameByServiceTypeNameByPrice();
}

