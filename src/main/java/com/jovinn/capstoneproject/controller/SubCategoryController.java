package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.SubCategory;
import com.jovinn.capstoneproject.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubCategoryController {
    @Autowired
    private final SubCategoryService subCategoryService;

    @GetMapping("/subCategory/findByCategory/{catId}")
    List<SubCategory> getByCategory(UUID catId){
        return subCategoryService.getSubCategoriesByCategory(catId);
    }

    @GetMapping("/subCategory/all")
    List<SubCategory> findAll(){
        return subCategoryService.getSubCategories();
    }

}
