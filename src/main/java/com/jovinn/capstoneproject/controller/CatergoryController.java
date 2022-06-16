package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CatergoryController {
    @Autowired
    private ServiceCategoryService categoryService;

    @PostMapping("/addCategory")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.saveServiceCategory(category);
    }

    @PostMapping("/addCategories")
    public List<Category> addCategories(@RequestBody List<Category> serviceCategories) {
        return categoryService.saveServiceCategories(serviceCategories);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getServiceCategories();
    }

    @GetMapping("/categoryById/{id}")
    public Category findProductById(@PathVariable UUID id) {
        return categoryService.getServiceCategoryById(id);
    }

    @GetMapping("/category/{name}")
    public Category findProductByName(@PathVariable String name) {
        return categoryService.getServiceCategoryByName(name);
    }

    @PutMapping("/update")
    public Category updateProduct(@RequestBody Category category) {
        return categoryService.updateServiceCategory(category);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteServiceCategoryById(id);
    }
}
