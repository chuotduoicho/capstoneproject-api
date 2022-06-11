package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SubCategoryRepository extends JpaRepository<SubCategory, UUID> {
    SubCategory findByName(String name);
    List<SubCategory> getSubCategoriesByCategory(UUID categoryId);
}
