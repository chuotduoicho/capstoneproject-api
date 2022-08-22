package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.SubCategory;

import java.util.List;
import java.util.UUID;

public interface SubCategoryService {
    SubCategory saveSubCategory(UUID categoryId, SubCategory subCategory);
    List<SubCategory> getSubCategories();
    List<SubCategory> getSubCategoriesByCategory(UUID categoryId);
    SubCategory updateCategory(SubCategory subCategory);
    String deleteSubcategory(UUID categoryId);
}
