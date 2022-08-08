package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchRequest;
import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.boxsearch.ListBoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.request.BoxRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.BoxResponse;
import com.jovinn.capstoneproject.dto.adminsite.CountServiceResponse;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BoxService {
    //Add Service
    ApiResponse addBox(Box box, UserPrincipal currentUser);
    //Update Service
    ApiResponse updateBox(UUID id, BoxRequest request, UserPrincipal currentUser);

    //Delete Service
    ApiResponse deleteBox(UUID id, UserPrincipal currentUser);

    //Change Status
    ApiResponse updateStatus(UUID boxId, UserPrincipal currentUser);

    //View List Service Buy SellerId
    List<Box> getAllServiceByCategoryID(UUID categoryId);

    //View All Service
    List<Box> getAllService();

    //Get Service By id
    BoxResponse getServiceByID(UUID id, UserPrincipal currentUser);

    //Get Service By Category ID
    PageResponse<BoxSearchResponse> getListServiceBySellerId(UUID sellerId, UserPrincipal currentUser,
                                                       BoxServiceStatus status, int page, int size);

    //Get Service By Category ID Pagination
    Page<Box> getAllServiceByCatIdPagination(int offset, UUID categoryId);

    //Search Service by Category Name, Sub Category Name
    Page<Box> searchServiceByCatNameBySubCateName(int offset, String catName, String serviceTypeName);

    CountServiceResponse countTotalService();

    CountServiceResponse countTotalServiceByCat(UUID catId);

    PageResponse<BoxSearchResponse> getBoxes(UUID categoryId, UUID subCategoryId,
                                             BigDecimal minPrice, BigDecimal maxPrice, int ratingPoint,
                                             String searchKeyWord, int page, int size, String sortBy, String sortDir);
    ListBoxSearchResponse search(BoxSearchRequest request);
    PageResponse<BoxSearchResponse> searchResult(String searchKeyWord, UUID categoryId, UUID subCategoryId,  BigDecimal minPrice, BigDecimal maxPrice, int ratingPoint,
                                                 int page, int size, String sortBy, String sortDir);
    List<BoxSearchResponse> getListHistoryBox(UserPrincipal currentUser);

    List<BoxSearchResponse> getTop8BoxByImpression();
    List<BoxSearchResponse> getTop8BoxByCategoryOrderByImpression(UUID categoryId);

}
