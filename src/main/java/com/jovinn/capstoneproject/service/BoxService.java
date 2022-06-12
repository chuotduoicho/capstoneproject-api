package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Box;

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
}
