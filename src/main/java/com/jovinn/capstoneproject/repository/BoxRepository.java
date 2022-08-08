package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.model.Box;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface BoxRepository extends JpaRepository<Box, UUID> {
    Page<Box> findAllBySellerIdAndStatus(UUID sellerId, BoxServiceStatus status, Pageable pageable);
    Page<Box> findAll(@Nullable Specification<Box> specification, Pageable pageable);

    @Query("select b from Box b where b.subCategory.category.id = :catId")
    List<Box> getAllServiceByCategoryId(@Param("catId") UUID catId);
    @Query("select b from Box b where b.subCategory.category.id = :catId")
    Page<Box> getAllServiceByCategoryId(@Param("catId") UUID catId, Pageable pageable);
    long countBySubCategory_Category_Id(UUID catId);

    Page<Box> findAllBySubCategory_Category_Id(UUID catId, PageRequest pageRequest);

    Page<Box> findAllBySubCategory_NameContainsOrSubCategory_Category_NameContains(String subCatName, String catName, PageRequest pageRequest);

    List<Box> findAllBySellerId(UUID sellerId);
    @Query("SELECT b from Box b " +
            "INNER JOIN Package p on p.box.id = b.id " +
            "INNER JOIN Seller s on s.id = b.seller.id " +
            "WHERE b.subCategory.id = :subCategoryId " +
            "AND (p.price > :minPrice and p.price < :maxPrice) " +
            "AND s.ratingPoint = :ratingPoint " +
            "AND CONCAT(s.brandName, s.descriptionBio, b.title) LIKE %:searchKeyWord% ")
    Page<Box> getBoxesByFilter(@Param("subCategoryId") UUID subCategoryId,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               @Param("ratingPoint") Integer ratingPoint,
                               @Param("searchKeyWord") String searchKeyWord,
                               Pageable pageable);
    @Query("SELECT b from Box b " +
            "WHERE b.status = :boxStatus " +
            "AND b.subCategory.id = :subCategoryId " +
            "AND b.title LIKE %:searchKeyWord%")
    Page<Box> findAllBySubCategoryAndSearchKeyWord(@Param("subCategoryId") UUID subCategoryId,
                                                   @Param("searchKeyWord") String searchKeyWord,
                                                   @Param("boxStatus") BoxServiceStatus boxStatus,
                                                   Pageable pageable);
    @Query("SELECT b from Box b " +
            "WHERE b.status = :boxStatus " +
            "AND b.title LIKE %:searchKeyWord% " +
            "AND b.subCategory.id = :subCategoryId " +
            "AND (b.fromPrice > :minPrice AND b.fromPrice < :maxPrice)")
    Page<Box> findAllBySubCategoryIdAndFromPriceBetweenAndTitleLike(@Param("subCategoryId") UUID subCategoryId,
                                                                    @Param("minPrice") BigDecimal minPrice,
                                                                    @Param("maxPrice") BigDecimal maxPrice,
                                                                    @Param("searchKeyWord") String searchKeyWord,
                                                                    @Param("boxStatus") BoxServiceStatus boxStatus,
                                                                    Pageable pageable);
    @Query("SELECT b from Box b " +
            "WHERE b.status = :boxStatus " +
            "AND b.title LIKE %:searchKeyWord% " +
            "AND (b.fromPrice > :minPrice AND b.fromPrice < :maxPrice)")
    Page<Box> findAllByFromPriceAndSearchKeyWord(@Param("minPrice") BigDecimal minPrice,
                                                 @Param("maxPrice") BigDecimal maxPrice,
                                                 @Param("searchKeyWord") String searchKeyWord,
                                                 @Param("boxStatus") BoxServiceStatus boxStatus,
                                                 Pageable pageable);

    @Query("SELECT b from Box b WHERE b.status = :boxStatus AND b.title LIKE %:searchKeyWord%")
    Page<Box> findAllByTitleLike(@Param("searchKeyWord") String searchKeyWord,
                                 @Param("boxStatus") BoxServiceStatus boxStatus,
                                 Pageable pageable);
    @Query("SELECT b FROM Box b WHERE b.status = ?1 ORDER BY b.impression DESC")
    List<Box> getTop8ByImpression(BoxServiceStatus status, PageRequest pageRequest);
    @Query("SELECT b FROM Box b WHERE b.status = ?1 AND b.subCategory.category.id = ?2 ORDER BY b.impression DESC")
    List<Box> getTop8BoxByCategoryOrderByImpression(BoxServiceStatus status, UUID categoryId, PageRequest pageRequest);
}
