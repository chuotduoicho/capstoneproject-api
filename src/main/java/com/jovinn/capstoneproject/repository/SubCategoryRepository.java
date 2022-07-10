package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, UUID> {
    SubCategory findByName(String name);
    List<SubCategory> findByCategoryId(UUID categoryId);

    SubCategory findSubCategoryById(UUID id);
}
