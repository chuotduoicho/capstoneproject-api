package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Box;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface BoxRepository extends JpaRepository<Box, UUID> {
    List<Box> findAllById(UUID sellerId);

    @Query("select b from Box b where b.serviceType.subCategory.category.id = :catId")
    List<Box> getAllServiceByCategoryId(@Param("catId") UUID catId);

    Page<Box> findAllByServiceType_SubCategory_Category_Id(UUID catId, PageRequest pageRequest);

    Page<Box> findAllByServiceType_NameOrServiceType_SubCategory_Category_Name(String serviceTypeName, String catName, PageRequest pageRequest);
//    @Query("select b from Box b where b.serviceType.subCategory.category.name = :catName or b.serviceType.name = :serviceTypeName or b.packages.")
//    List<Box> searchAllServiceByCatNameByServiceTypeNameByPrice();
}
