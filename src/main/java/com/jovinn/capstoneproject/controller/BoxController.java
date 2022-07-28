package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.BoxResponse;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.util.WebConstant;
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
@RequestMapping("/api/v1/box")
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
    public ResponseEntity<BoxResponse> updateService(@RequestParam("id")UUID id,
                                                     @RequestBody Box box, @CurrentUser UserPrincipal currentUser){
        BoxResponse response = boxService.updateBox(box, id, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Api delete Service
    @DeleteMapping("/delete-service/{id}")
    public boolean deleteBox(@PathVariable UUID id){
        return boxService.deleteBox(id);
    }

    //Api get Service By Seller Id
    @GetMapping("/list-service-by-sellerId/{sellerId}")
    public ResponseEntity<PageResponse<BoxResponse>> getBoxServiceBySellerId(@PathVariable UUID sellerId,
                                                @RequestParam(name = "page", required = false,
                                                        defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                                @RequestParam(name = "size", required = false,
                                                        defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size){
        PageResponse<BoxResponse> response = boxService.getListServiceBySellerId(sellerId, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Api get all Service
//    @GetMapping("/list-service")
//    public List<Box> getAllService(){
//        return boxService.getAllService();
//    }

    @GetMapping("/box-services")
    public ResponseEntity<PageResponse<BoxResponse>> getAllPosts(@RequestParam(name = "page", required = false,
                                                    defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                         @RequestParam(name = "size", required = false,
                                                    defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size,
                                         @RequestParam(value = "sortBy",
                                                    defaultValue = WebConstant.DEFAULT_SORT_BY, required = false) String sortBy,
                                         @RequestParam(value = "sortDir",
                                                    defaultValue = WebConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PageResponse<BoxResponse> response = boxService.getAllService(page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
//        return boxService.getAllService(page, size, sortBy, sortDir).stream().map(box -> modelMapper.map(box, BoxResponse.class))
//                .collect(Collectors.toList());
    }
    //Api view detail Service
    @GetMapping("/box-details/{id}")
    public ResponseEntity<BoxResponse> getServiceById(@PathVariable UUID id){
        BoxResponse response = boxService.getServiceByID(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list-services-by-cat/{catId}")
    public ResponseEntity<PageResponse<BoxResponse>> getAllServiceByCategoryId(@PathVariable("catId") UUID catId,
                                                       @RequestParam(name = "page", required = false,
                                                               defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                                       @RequestParam(name = "size", required = false,
                                                               defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size,
                                                       @RequestParam(value = "sortBy",
                                                               defaultValue = WebConstant.DEFAULT_SORT_BY, required = false) String sortBy,
                                                       @RequestParam(value = "sortDir",
                                                               defaultValue = WebConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PageResponse<BoxResponse> response = boxService.getAllServiceByCategoryID(catId, page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
//        return boxService.getAllServiceByCategoryID(catId).stream().map(box -> modelMapper.map(box, BoxResponse.class))
//                .collect(Collectors.toList());
    }

    @GetMapping("/paginate-list-services-by-cat/{catId}/{page}")
    public Page<Box> getAllServiceByCategoryIdPagination(@PathVariable UUID catId, @PathVariable int page){
        return  boxService.getAllServiceByCatIdPagination(page,catId);
    }

    @GetMapping("/searchAllServiceByCatNameBySubCateName/{name}/{page}")
    public Page<Box> searchAllServiceByCatNameByServiceTypeName(@PathVariable String name, @PathVariable int page){
        return boxService.searchServiceByCatNameBySubCateName(page,name,name);
    }
}
