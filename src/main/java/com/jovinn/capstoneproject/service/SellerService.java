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
    Seller updateSeller(Seller seller, UUID id);
    //Using for admin
    Seller deleteSeller(UUID id);
}
