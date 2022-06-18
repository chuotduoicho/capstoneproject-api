package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface BoxService {
    //Add Service
    Box saveBox(Box box);

    //Update Service
    Box updateBox(Box box, UUID id);

    //Delete Service
    Boolean deleteBox(UUID id);

    //Change Status
    Box updateStatus(UUID id);

    //View List Service Buy SellerId
    List<Box> getListServiceBySellerId(UUID sellerId);

    //View All Service
    List<Box> getAllService();

    //Get Service By id
    Box getServiceByID(UUID id);

    //Get Service By Category ID
    List<Box> getAllServiceByCategoryID(UUID categoryId);

    //Get Service By Category ID Pagination
    Page<Box> getAllServiceByCatIdPagination(int offset, UUID categoryId);

    //Search Service by Category Name, Service Type Name
    Page<Box> searchServiceByCatNameByServiceTypeName(int offset, String catName, String serviceTypeName);
}
