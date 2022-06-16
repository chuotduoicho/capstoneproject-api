package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public Seller saveSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    @Override
    public List<Seller> getSellers() {
        return sellerRepository.findAll();
    }

    @Override
    public List<Seller> getListInfoBySellerId(UUID sellerId) {
        return null;
    }

    @Override
    public Seller getSellerById(UUID id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "seller not found", id ));
    }

    @Override
    public Seller getSellerBySellerNumber(String sellerNumber) {
        return sellerRepository.findBySellerNumber(sellerNumber);
    }

    @Override
    public Seller updateSeller(Seller seller, UUID id) {
        return null;
    }

    @Override
    public Seller deleteSeller(UUID id) {
        return null;
    }

//    @Override
//    public Seller updateSeller(Seller seller) {
//        Seller existSeller = sellerRepository.findById(seller.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("seller", "seller not exist", seller));
//        existSeller.setDescriptionBio(seller.getDescriptionBio());
//        return sellerRepository.save(seller);
//    }
}
