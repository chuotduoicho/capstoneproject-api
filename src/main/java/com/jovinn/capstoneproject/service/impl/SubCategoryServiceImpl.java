package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.SubCategory;
import com.jovinn.capstoneproject.repository.SubCategoryRepository;
import com.jovinn.capstoneproject.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    SubCategoryRepository subCategoryRepository;

    @Override
    public SubCategory saveSubCategory(SubCategory subCategory) {
        return subCategoryRepository.save(subCategory);
    }

    @Override
    public List<SubCategory> getSubCategories() {
        return subCategoryRepository.findAll();
    }

    @Override
    public List<SubCategory> getSubCategoriesByCategory(UUID categoryId) {
        return subCategoryRepository.findByCategoryId(categoryId);
    }

    @Override
    public SubCategory updateCategory(SubCategory subCategory) {
        return null;
    }

    @Override
    public String deleteSubcategory(UUID subCategoryId) {
        subCategoryRepository.deleteById(subCategoryId);
        return "deleted sub_category "+ subCategoryId;
    }
}
