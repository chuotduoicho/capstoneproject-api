package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchRequest;
import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.boxsearch.ListBoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.request.BoxRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.BoxResponse;
import com.jovinn.capstoneproject.dto.client.response.RatingResponse;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.service.RatingService;
import com.jovinn.capstoneproject.util.WebConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
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
    @Autowired
    private RatingService ratingService;
    //API add Service
    @PostMapping("/add-box-service")
    public ResponseEntity<ApiResponse> addBox(@RequestBody Box box, @CurrentUser UserPrincipal currentUser){
        return new ResponseEntity<>(boxService.addBox(box, currentUser), HttpStatus.CREATED);
    }

    //API update Service
    @PutMapping("/update-service")
    public ResponseEntity<ApiResponse> updateService(@RequestParam("id")UUID id,
                                                     @RequestBody BoxRequest request, @CurrentUser UserPrincipal currentUser){
        return new ResponseEntity<>(boxService.updateBox(id, request, currentUser), HttpStatus.OK);
    }

    //Api delete Service
    @DeleteMapping("/delete-service/{id}")
    public ResponseEntity<ApiResponse> deleteBox(@PathVariable UUID id, @CurrentUser UserPrincipal currentUser) {
        return new ResponseEntity<>(boxService.deleteBox(id, currentUser), HttpStatus.OK);
    }

    @GetMapping("/top-8-impression")
    public ResponseEntity<List<BoxSearchResponse>> getTop8BoxByTotalContract() {
        return new ResponseEntity<>(boxService.getTop8BoxByTotalContract(), HttpStatus.OK);
    }

    @GetMapping("/top-8-impression/{categoryId}")
    public ResponseEntity<List<BoxSearchResponse>> getTop8BoxByCategoryOrderByImpression(@PathVariable("categoryId") UUID categoryId) {
        return new ResponseEntity<>(boxService.getTop8BoxByCategoryOrderByTotalContract(categoryId), HttpStatus.OK);
    }

    @GetMapping("/list-box-history")
    public ResponseEntity<List<BoxSearchResponse>> getListBoxHistory(@CurrentUser UserPrincipal currentUser) {
        return new ResponseEntity<>(boxService.getListHistoryBox(currentUser), HttpStatus.OK);
    }

    @GetMapping("/list-box")
    public ResponseEntity<PageResponse<BoxSearchResponse>> getBoxes(@RequestParam(name = "categoryId", required = false,
                                                                      defaultValue = "") UUID categoryId,
                                                              @RequestParam(name = "subCategoryId", required = false,
                                                                      defaultValue = "") UUID subCategoryId,
                                                              @RequestParam(name = "minPrice", required = false,
                                                                      defaultValue = WebConstant.MIN_PRICE) BigDecimal minPrice,
                                                              @RequestParam(name = "maxPrice", required = false,
                                                                      defaultValue = WebConstant.MAX_PRICE) BigDecimal maxPrice,
                                                              @RequestParam(name = "ratingPoint", required = false,
                                                                      defaultValue = WebConstant.MAX_PRICE) Integer ratingPoint,
                                                              @RequestParam(name = "searchKeyWord", required = false,
                                                                      defaultValue = "") String searchKeyWord,
                                                              @RequestParam(name = "page", required = false,
                                                                      defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                                              @RequestParam(name = "size", required = false,
                                                                      defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size,
                                                              @RequestParam(value = "sortBy",
                                                                      defaultValue = WebConstant.DEFAULT_BOX_SORT_BY, required = false) String sortBy,
                                                              @RequestParam(value = "sortDir",
                                                                      defaultValue = WebConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        PageResponse<BoxSearchResponse> response = boxService.getBoxes(categoryId, subCategoryId, minPrice, maxPrice, ratingPoint,
                                                                searchKeyWord, page, size, sortBy, sortDir);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/search-and-filter")
    public ResponseEntity<ListBoxSearchResponse> search(@Valid @RequestBody BoxSearchRequest request) {
        return new ResponseEntity<>(boxService.search(request), HttpStatus.OK);
    }

    @GetMapping("/search/{searchKeyWord}")
    public ResponseEntity<PageResponse<BoxSearchResponse>> searchBySearchKeyWord(@PathVariable("searchKeyWord") String searchKeyWord,
                                                                                 @RequestParam(name = "categoryId", required = false) UUID categoryId,
                                                                                 @RequestParam(name = "subCategoryId", required = false) UUID subCategoryId,
                                                                                 @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
                                                                                 @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
                                                                                 @RequestParam(name = "ratingPoint", required = false,
                                                                                         defaultValue = WebConstant.MAX_PRICE) Integer ratingPoint,
                                                                                 @RequestParam(name = "page", required = false,
                                                                                         defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                                                                 @RequestParam(name = "size", required = false,
                                                                                         defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size,
                                                                                 @RequestParam(value = "sortBy",
                                                                                         defaultValue = WebConstant.DEFAULT_BOX_SORT_BY, required = false) String sortBy,
                                                                                 @RequestParam(value = "sortDir",
                                                                                         defaultValue = WebConstant.DEFAULT_SORT_DIRECTION, required = false) String sortDir) {
        return new ResponseEntity<>(
                boxService.searchResult(searchKeyWord, categoryId, subCategoryId,
                        minPrice, maxPrice, ratingPoint,
                        page, size, sortBy, sortDir),
                HttpStatus.OK
        );
    }

    //Api get Service By Seller Id
    @GetMapping("/list-service-by-sellerId/{sellerId}")
    public ResponseEntity<PageResponse<BoxSearchResponse>> getBoxServiceBySellerId(@PathVariable UUID sellerId, @CurrentUser UserPrincipal currentUser,
                                                                             @RequestParam(name = "status", required = false,
                                                                                     defaultValue = WebConstant.DEFAULT_BOX_STATUS) BoxServiceStatus status,
                                                                             @RequestParam(name = "page", required = false,
                                                                                     defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page,
                                                                             @RequestParam(name = "size", required = false,
                                                                                     defaultValue = WebConstant.DEFAULT_PAGE_SIZE) Integer size){
        return new ResponseEntity<>(
                boxService.getListServiceBySellerId(sellerId, currentUser, status, page, size),
                HttpStatus.OK
        );
    }

    @PutMapping("/update-status/{boxId}")
    public ResponseEntity<ApiResponse> updateBoxStatus(@PathVariable("boxId") UUID boxId,
                                                      @CurrentUser UserPrincipal currentUser){
        return new ResponseEntity<>(boxService.updateStatus(boxId, currentUser), HttpStatus.OK);
    }

    @GetMapping("/box-services")
    public ResponseEntity<List<BoxResponse>> getAllPosts() {
        List<BoxResponse> responses = boxService.getAllService().stream()
                .map(box -> modelMapper.map(box, BoxResponse.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
    //Api view detail Service
    @GetMapping("/box-details/{id}")
    public ResponseEntity<BoxResponse> getServiceById(@PathVariable("id") UUID id,
                                                      @CurrentUser UserPrincipal currentUser){
        return new ResponseEntity<>(boxService.getServiceByID(id, currentUser), HttpStatus.OK);
    }

    @GetMapping("/box-details-guest/{id}")
    public ResponseEntity<BoxResponse> getBoxByIdForGuest(@PathVariable("id") UUID id){
        return new ResponseEntity<>(boxService.getBoxByIdForGuest(id), HttpStatus.OK);
    }

    @GetMapping("/list-services-by-cat/{catId}")
    public ResponseEntity<List<BoxResponse>> getAllServiceByCategoryId(@PathVariable("catId") UUID catId) {
        List<BoxResponse> response = boxService.getAllServiceByCategoryID(catId).stream()
                .map(box -> modelMapper.map(box, BoxResponse.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
//        return boxService.getAllServiceByCategoryID(catId).stream().map(box -> modelMapper.map(box, BoxResponse.class))
//                .collect(Collectors.toList());
    }
    @GetMapping("/rating-top3/{boxId}")
    public ResponseEntity<List<RatingResponse>> getRatingsForSeller(@PathVariable("boxId") UUID boxId) {
        return new ResponseEntity<>(ratingService.getTop3Ratings(boxId), HttpStatus.OK);
    }

    @GetMapping("/rating-list/{boxId}")
    public ResponseEntity<List<RatingResponse>> getRatingForBoxService(@PathVariable("boxId") UUID boxId,
                                                                       @RequestParam(value = "page", required = false,
                                                                               defaultValue = WebConstant.DEFAULT_PAGE_NUMBER) Integer page){
        return new ResponseEntity<>(ratingService.getRatingsForBox(boxId, page), HttpStatus.OK);
    }

    @GetMapping("/paginate-list-services-by-cat/{catId}/{page}")
    public Page<Box> getAllServiceByCategoryIdPagination(@PathVariable UUID catId, @PathVariable int page){
        return  boxService.getAllServiceByCatIdPagination(page, catId);
    }

    @GetMapping("/searchAllServiceByCatNameBySubCateName/{name}/{page}")
    public Page<Box> searchAllServiceByCatNameByServiceTypeName(@PathVariable String name, @PathVariable int page){
        return boxService.searchServiceByCatNameBySubCateName(page, name, name);
    }
}
