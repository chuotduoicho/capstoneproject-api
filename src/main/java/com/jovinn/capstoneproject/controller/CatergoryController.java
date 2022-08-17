package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CatergoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/category")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.saveCategory(category);
    }

    @PostMapping("/categories")
    public List<Category> addCategories(@RequestBody List<Category> serviceCategories) {
        return categoryService.saveCategories(serviceCategories);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/category/{id}")
    public Category findProductById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/category/{name}")
    public Category findProductByName(@PathVariable String name) {
        return categoryService.getCategoryByName(name);
    }

    @PutMapping("/category/update")
    public Category updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteCategoryById(id);
    }
}
