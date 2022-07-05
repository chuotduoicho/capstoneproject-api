package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.BoxResponse;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class BoxController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BoxService boxService;

    //API add Service
    @PostMapping("/add-box-service")
    public ResponseEntity<ApiResponse> addService(@RequestBody Box box, @CurrentUser UserPrincipal currentUser){
        ApiResponse response = boxService.saveBox(box, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //API update Service
    @PutMapping("/update-service")
    public Box updateService(@RequestParam("id")UUID id,@RequestBody Box box){
        return boxService.updateBox(box,id);
    }

    //Api delete Service
    @DeleteMapping("/delete-service/{id}")
    public boolean deleteBox(@PathVariable UUID id){
        return boxService.deleteBox(id);
    }

    //Api get Service By Seller Id
    @GetMapping("/list-service-by-sellerId/{sellerId}")
    public List<Box> getBoxServiceBySellerId(@PathVariable UUID sellerId){
        return boxService.getListServiceBySellerId(sellerId);
    }

    //Api get all Service
//    @GetMapping("/list-service")
//    public List<Box> getAllService(){
//        return boxService.getAllService();
//    }

    @GetMapping("/box-services")
    public List<BoxResponse> getAllPosts() {
        return boxService.getAllService().stream().map(box -> modelMapper.map(box, BoxResponse.class))
                .collect(Collectors.toList());
    }
    //Api view detail Service
    @GetMapping("/box-details/{id}")
    public Box getServiceById(@PathVariable UUID id){
        return boxService.getServiceByID(id);
    }

    @GetMapping("/list-services-by-cat/{catId}")
    public List<Box> getAllServiceByCategoryId(@PathVariable("catId") UUID catId){
        return boxService.getAllServiceByCategoryID(catId);
    }

    @GetMapping("/paginate-list-services-by-cat/{catId}/{page}")
    public Page<Box> getAllServiceByCategoryIdPagination(@PathVariable UUID catId,@PathVariable int page){
        return  boxService.getAllServiceByCatIdPagination(page,catId);
    }

    @GetMapping("/searchAllServiceByCatNameBySubCateName/{name}/{page}")
    public Page<Box> searchAllServiceByCatNameByServiceTypeName(@PathVariable String name, @PathVariable int page){
        return boxService.searchServiceByCatNameBySubCateName(page,name,name);
    }
}
