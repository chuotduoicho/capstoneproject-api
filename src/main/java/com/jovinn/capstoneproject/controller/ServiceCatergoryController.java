package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.ServiceCategory;
import com.jovinn.capstoneproject.service.ServiceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ServiceCatergoryController {
    @Autowired
    private ServiceCategoryService categoryService;

    @PostMapping("/addCategory")
    public ServiceCategory addCategory(@RequestBody ServiceCategory serviceCategory) {
        return categoryService.saveServiceCategory(serviceCategory);
    }

    @PostMapping("/addCategories")
    public List<ServiceCategory> addCategories(@RequestBody List<ServiceCategory> serviceCategories) {
        return categoryService.saveServiceCategories(serviceCategories);
    }

    @GetMapping("/categories")
    public List<ServiceCategory> getAllCategories() {
        return categoryService.getServiceCategories();
    }

    @GetMapping("/categoryById/{id}")
    public ServiceCategory findProductById(@PathVariable UUID id) {
        return categoryService.getServiceCategoryById(id);
    }

    @GetMapping("/category/{name}")
    public ServiceCategory findProductByName(@PathVariable String name) {
        return categoryService.getServiceCategoryByName(name);
    }

    @PutMapping("/update")
    public ServiceCategory updateProduct(@RequestBody ServiceCategory serviceCategory) {
        return categoryService.updateServiceCategory(serviceCategory);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCategory(@PathVariable UUID id) {
        return categoryService.deleteServiceCategoryById(id);
    }
}
