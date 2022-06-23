package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.CertificateRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EnumType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;

    @Autowired
    private CertificateRepository certificateRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found ", id ));
    }

    @Override
    public Seller getSellerBySellerNumber(String sellerNumber) {
        return sellerRepository.findBySellerNumber(sellerNumber);
    }

    @Override
    public Seller updateSeller(UUID id, Seller seller, UserPrincipal currentUser) {
        Seller existSeller = sellerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Seller Not found by ", id));
        User user = existSeller.getUser();
        if(user.getId().equals(currentUser.getId())) {
            existSeller.setDescriptionBio(seller.getDescriptionBio());
            existSeller.setLanguages(new ArrayList<>());
            existSeller.setCertificates(new ArrayList<>());
            existSeller.setSkills(new ArrayList<>());
            existSeller.setEducations(new ArrayList<>());
            existSeller.setUrlProfiles(new ArrayList<>());
            return sellerRepository.save(existSeller);
        } else {
            ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: " + user.getUsername());
            throw new UnauthorizedException(apiResponse);
        }
    }

    @Override
    public Seller getSellerByUserId(UUID userId) {
        return sellerRepository.findSellerByUserId(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "UserId can't duplicate", userId));
    }

    @Override
    public Seller deleteSeller(UUID id) {
        return null;
    }

    @Override
    public List<Seller> getListTopSellerByRank(RankSeller Rank) {
        return sellerRepository.findTop3ByRankSeller(Rank);
    }

//    @Override
//    public Seller updateSeller(Seller seller) {
//        Seller existSeller = sellerRepository.findById(seller.getId())
//                .orElseThrow(() -> new ResourceNotFoundException("seller", "seller not exist", seller));
//        existSeller.setDescriptionBio(seller.getDescriptionBio());
//        return sellerRepository.save(seller);
//    }
}
