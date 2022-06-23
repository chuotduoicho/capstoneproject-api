package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface SellerService {
    Seller saveSeller(Seller seller);
    List<Seller> getSellers();
    List<Seller> getListInfoBySellerId(UUID sellerId);
    Seller getSellerById(UUID id);
    Seller getSellerBySellerNumber(String sellerNumber);
    Seller updateSeller(UUID id, Seller seller);
    ApiResponse becomeSeller(UUID id, Seller seller, UserPrincipal currentUser);
    Seller getSellerByUserId(UUID userId);
    //Using for admin
    Seller deleteSeller(UUID id);
}
