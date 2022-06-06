package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.repository.CategoryRepository;
import com.jovinn.capstoneproject.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements ServiceCategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category saveServiceCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> saveServiceCategories(List<Category> serviceCategories) {
        return categoryRepository.saveAll(serviceCategories);
    }

    @Override
    public List<Category> getServiceCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getServiceCategoryById(UUID id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category getServiceCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public String deleteServiceCategoryById(UUID id) {
        categoryRepository.deleteById(id);
        return "Category remove" + id;
    }

    @Override
    public Category updateServiceCategory(Category category) {
        Category existCategory = categoryRepository.findById(category.getId()).orElse(null);
        existCategory.setName(category.getName());
        return categoryRepository.save(existCategory);
    }
}
