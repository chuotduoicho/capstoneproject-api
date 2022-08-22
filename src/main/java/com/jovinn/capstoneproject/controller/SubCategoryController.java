package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.model.SubCategory;
import com.jovinn.capstoneproject.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubCategoryController {
    @Autowired
    private final SubCategoryService subCategoryService;

    @GetMapping("/subCategory/findByCategory/{catId}")
    public ResponseEntity<List<SubCategory>> getByCategory(@PathVariable("catId") UUID catId){
        return new ResponseEntity<>(subCategoryService.getSubCategoriesByCategory(catId), HttpStatus.OK);
    }

    @GetMapping("/subCategories")
    public ResponseEntity<List<SubCategory>> getAllCategory(){
        return new ResponseEntity<>(subCategoryService.getSubCategories(), HttpStatus.OK);
    }

    @PostMapping("/subCategory/{catId}")
    public ResponseEntity<SubCategory> saveSubCategory(@PathVariable("catId") UUID catId,
                                                       @RequestBody SubCategory subCategory){
        return new ResponseEntity<>(subCategoryService.saveSubCategory(catId, subCategory), HttpStatus.OK);
    }
}
