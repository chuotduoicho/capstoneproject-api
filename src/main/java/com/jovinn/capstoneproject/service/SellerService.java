package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Seller;

import java.util.List;
import java.util.UUID;

public interface SellerService {
    Seller saveSeller(Seller seller);
    List<Seller> getSellers();
    List<Seller> getListInfoBySellerId(UUID sellerId);
    Seller getSellerById(UUID id);
    Seller getSellerBySellerNumber(String sellerNumber);
    void updateSeller(UUID id, Seller seller);
    //Using for admin
    Seller deleteSeller(UUID id);
}
