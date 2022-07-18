package com.jovinn.capstoneproject.repository;

import com.jovinn.capstoneproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Category findByName(String name);
    Category findCategoryById(UUID id);
}
