package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EnumType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.jovinn.capstoneproject.util.GenerateRandomNumber.getRandomNumberString;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityTypeRepository activityTypeRepository;

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
    public ApiResponse becomeSeller(UUID id, Seller seller, UserPrincipal currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("", "", id));
        if (Boolean.TRUE.equals(sellerRepository.existsByUserId(user.getId()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bạn đã đăng ky là seller");
        }
        seller.setUser(user);
        seller.setRankSeller(RankSeller.BEGINNER);
        seller.setSellerNumber(getRandomNumberString());
        seller.setVerifySeller(Boolean.TRUE);
        seller.setTotalOrderFinish(0);
        user.setSeller(seller);
        user.setActivityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER));
        user.setJoinSellingAt(new Date());
        sellerRepository.save(seller);
        return new ApiResponse(Boolean.TRUE, user.getFirstName() + " Bạn đã trở thành người làm việc");
    }

    @Override
    public Seller getSellerBySellerNumber(String sellerNumber) {
        return sellerRepository.findBySellerNumber(sellerNumber);
    }

    @Override
    public Seller updateSeller(UUID id, Seller editSeller) {
        Seller existSeller = sellerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Seller Not found by ", id));
//        User user = currentUser.getId();
//        if(existSeller.getId().equals(currentUser.getId())) {
//        }
        existSeller.setDescriptionBio(editSeller.getDescriptionBio());

        return sellerRepository.save(existSeller);
//        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update profile of: ");
//        throw new UnauthorizedException(apiResponse);
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
    public List<Seller> getListTopSellerByRank() {
        return sellerRepository.findTop3ByRankSeller();
    }
}
