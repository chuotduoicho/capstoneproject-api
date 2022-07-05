package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.SubCategory;
import com.jovinn.capstoneproject.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    List<SubCategory> getByCategory(@PathVariable("catId") UUID catId){
        return subCategoryService.getSubCategoriesByCategory(catId);
    }

    @GetMapping("/subCategories")
    List<SubCategory> getAllCategory(){
        return subCategoryService.getSubCategories();
    }
}
