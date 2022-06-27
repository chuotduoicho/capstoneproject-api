package com.jovinn.capstoneproject.controller;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Category;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.service.CategoryService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class BoxController {
    @Autowired
    private BoxService boxService;

    @Autowired
    private CategoryService categoryService;

    //API add Service
    @PostMapping("/addService")
    public Box addService(@RequestBody Box box){
//        Category category = categoryService.deleteServiceCategoryById()
//        Box temp = new Box();
//        temp.setId(box.getId());
//        for(Package pack : box.getPackages()){
//            pack.setBox(temp);
//        }
//        box.setCategory(categoryService.getServiceCategoryById(category.getId()));
        return boxService.saveBox(box);
    }

    //API update Service
    @PutMapping("/updateService")
    public Box updateService(@RequestParam("id")UUID id,@RequestBody Box box){
        return boxService.updateBox(box,id);
    }

    //Api delete Service
    @DeleteMapping("/deleteService/{id}")
    public boolean deleteBox(@PathVariable UUID id){
        return boxService.deleteBox(id);
    }

    //Api get Service By Seller Id
    @GetMapping("/listServiceBySellerId/{sellerId}")
    public List<Box> getBoxServiceBySellerId(@PathVariable UUID sellerId){
        return boxService.getListServiceBySellerId(sellerId);
    }

    //Api get all Service
    @GetMapping("/listAllService")
    public List<Box> getAllService(){
        return boxService.getAllService();
    }

    //Api view detail Service
    @GetMapping("/serviceDetail/{id}")
    public Box getServiceById(@PathVariable UUID id){
        return boxService.getServiceByID(id);
    }

    @GetMapping("/getAllServiceByCatId/{catId}")
    public List<Box> getAllServiceByCategoryId(@PathVariable("catId") UUID catId){
        return boxService.getAllServiceByCategoryID(catId);
    }

    @GetMapping("/getAllServiceByCatIdPagination/{catId}/{page}")
    public Page<Box> getAllServiceByCategoryIdPagination(@PathVariable UUID catId,@PathVariable int page){
        return  boxService.getAllServiceByCatIdPagination(page,catId);
    }

    @GetMapping("/searchAllServiceByCatNameBySubCateName/{name}/{page}")
    public Page<Box> searchAllServiceByCatNameByServiceTypeName(@PathVariable String name, @PathVariable int page){
        return boxService.searchServiceByCatNameBySubCateName(page,name,name);
    }
}
