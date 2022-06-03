package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.repository.CategoryRepository;
import com.jovinn.capstoneproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> saveCategories(List<Category> categories) {
        return categoryRepository.saveAll(categories);
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(UUID id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public String deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
        return "Category remove" + id;
    }

    @Override
    public Category updateCategory(Category category) {
        Category existCategory = categoryRepository.findById(category.getId()).orElse(null);
        existCategory.setName(category.getName());
        return categoryRepository.save(existCategory);
    }
}
