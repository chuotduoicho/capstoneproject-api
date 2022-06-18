package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CatergoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addCategory")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    @PostMapping("/addCategories")
    public List<Category> addCategories(@RequestBody List<Category> serviceCategories) {
        return categoryService.saveCategories(serviceCategories);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/categoryById/{id}")
    public Category findProductById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/category/{name}")
    public Category findProductByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    @PutMapping("/update")
    public Category updateProduct(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteCategoryById(id);
    }
}
