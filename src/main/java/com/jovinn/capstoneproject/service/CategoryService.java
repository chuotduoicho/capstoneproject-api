package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category saveCategory(Category category);
    List<Category> saveCategories(List<Category> categories);
    List<Category> getCategories();
    Category getCategoryById(UUID id);
    Category getCategoryByName(String name);
    String deleteCategory(UUID id);
    Category updateCategory(Category category);
}
