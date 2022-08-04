package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface SellerService {
    Seller saveSeller(Seller seller);
    List<Seller> getSellers();
    List<Seller> getListInfoBySellerId(UUID sellerId);
    Seller getSellerById(UUID id);
    Seller getSellerByBrandName(String brandName);
    Seller updateSeller(Seller seller, UserPrincipal currentUser);
    Seller becomeSeller(Seller seller, UserPrincipal currentUser);
    Seller getSellerByUserId(UUID userId);
    //Using for admin
    Seller deleteSeller(UUID id);

    //View list seller buy rank
    List<Seller> getListTopSellerByRank();

    List<Seller> getListSellerBuyPostRequestId(UUID postRequestId);
}
