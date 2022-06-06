package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Category;

import java.util.List;
import java.util.UUID;

public interface ServiceCategoryService {
    Category saveServiceCategory(Category category);
    List<Category> saveServiceCategories(List<Category> serviceCategories);
    List<Category> getServiceCategories();
    Category getServiceCategoryById(UUID id);
    Category getServiceCategoryByName(String name);
    String deleteServiceCategoryById(UUID id);
    Category updateServiceCategory(Category category);
}
