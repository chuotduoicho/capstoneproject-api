package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.BoxResponse;
import com.jovinn.capstoneproject.dto.adminsite.CountServiceResponse;
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
    List<Box> getListServiceBySellerId(UUID sellerId);

    //View All Service
    List<Box> getAllService();

    //Get Service By id
    BoxResponse getServiceByID(UUID id);

    //Get Service By Category ID
    List<Box> getAllServiceByCategoryID(UUID categoryId);

    //Get Service By Category ID Pagination
    Page<Box> getAllServiceByCatIdPagination(int offset, UUID categoryId);

    //Search Service by Category Name, Sub Category Name
    Page<Box> searchServiceByCatNameBySubCateName(int offset, String catName, String serviceTypeName);

    CountServiceResponse countTotalService();

    CountServiceResponse countTotalServiceByCat(UUID catId);
}
