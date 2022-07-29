package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.request.PackageRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.BoxResponse;
import com.jovinn.capstoneproject.dto.response.CountServiceResponse;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BoxService {
    //Add Service
    ApiResponse saveBox(Box box, UserPrincipal currentUser);
    //Update Service
    BoxResponse updateBox(Box box, UUID id, UserPrincipal currentUser);

    //Delete Service
    Boolean deleteBox(UUID id);

    //Change Status
    Box updateStatus(UUID id);

    //View List Service Buy SellerId
    PageResponse<BoxResponse> getListServiceBySellerId(UUID sellerId, int page, int size);

    //View All Service
    PageResponse<BoxResponse> getAllService(int page, int size, String sortBy, String sortDir);

    //Get Service By id
    BoxResponse getServiceByID(UUID id);

    //Get Service By Category ID
    PageResponse<BoxResponse> getAllServiceByCategoryID(UUID categoryId, int page, int size, String sortBy, String sortDir);

    //Get Service By Category ID Pagination
    Page<Box> getAllServiceByCatIdPagination(int offset, UUID categoryId);

    //Search Service by Category Name, Sub Category Name
    Page<Box> searchServiceByCatNameBySubCateName(int offset, String catName, String serviceTypeName);

    CountServiceResponse countTotalService();

    CountServiceResponse countTotalServiceByCat(UUID catId);
}
