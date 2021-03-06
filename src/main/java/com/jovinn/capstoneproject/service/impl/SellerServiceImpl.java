package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.*;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.jovinn.capstoneproject.util.GenerateRandom.getRandomNumberString;

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
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Seller not found "));
    }

    @Override
    public Seller getSellerByBrandName(String brandName) {
        return sellerRepository.findSellerByBrandName(brandName);
    }

    @Override
    public Seller becomeSeller(Seller seller, UserPrincipal currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Kh??ng t??m th???y t??i kho???n c???a b???n: " + currentUser.getId()));
        if (Boolean.TRUE.equals(sellerRepository.existsByUserId(user.getId()))) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "B???n ???? ????ng k?? l?? seller");
        } else if (user.getPhoneNumber().isBlank() || user.getCity().isBlank() || user.getCountry().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "B???n c???n ho??n t???t th??ng tin c?? b???n tr?????c");
        }

        String randomNumber;
        do {
            randomNumber = getRandomNumberString();
        } while (Boolean.TRUE.equals(sellerRepository.existsBySellerNumber(randomNumber)));

        try {
            if (user.getIsEnabled().equals(Boolean.TRUE)) {
                seller.setUser(user);
                seller.setRankSeller(RankSeller.BEGINNER);
                seller.setSellerNumber(randomNumber);
                seller.setVerifySeller(Boolean.TRUE);
                seller.setTotalOrderFinish(0);
                seller.setRatingPoint(0);
                user.setSeller(seller);
                user.setActivityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER));
                user.setJoinSellingAt(new Date());

                return sellerRepository.save(seller);
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "C???n x??c th???c t??i kho???n tr?????c khi tr??? th??nh ng?????i b??n");
            }
        } catch (BadRequestException e) {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "???? x???y ra l???i");
        }
    }

    @Override
    public Seller updateSeller(Seller editSeller, UserPrincipal currentUser) {
        Seller existSeller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "kh??ng t??m th???y t??i kho???n ng?????i b??n"));

        if (existSeller.getUser().getId().equals(currentUser.getId())) {
            existSeller.setDescriptionBio(editSeller.getDescriptionBio());
            existSeller.setBrandName(editSeller.getBrandName());
            return sellerRepository.save(existSeller);
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to update");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Seller getSellerByUserId(UUID userId) {
        return sellerRepository.findSellerByUserId(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Not found"));
    }

    @Override
    public Seller deleteSeller(UUID id) {
        return null;
    }

    @Override
    public List<Seller> getListTopSellerByRank() {
        return sellerRepository.findTop3ByRankSeller();
    }

    @Override
    public List<Seller> getListSellerBuyPostRequestId(UUID postRequestId) {
        return sellerRepository.findAllByPostRequests_Id(postRequestId);
    }
}
