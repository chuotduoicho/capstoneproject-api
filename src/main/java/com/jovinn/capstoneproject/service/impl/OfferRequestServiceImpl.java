package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.OfferRequestRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.OfferRequestResponse;
import com.jovinn.capstoneproject.enumerable.OfferRequestStatus;
import com.jovinn.capstoneproject.enumerable.OfferType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.OfferRequest;
import com.jovinn.capstoneproject.model.PostRequest;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.OfferRequestRepository;
import com.jovinn.capstoneproject.repository.PostRequestRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.OfferRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfferRequestServiceImpl implements OfferRequestService {
    @Autowired
    private OfferRequestRepository offerRequestRepository;
    @Autowired
    private PostRequestRepository postRequestRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Override
    public OfferRequestResponse sendOfferToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy post cần gửi offer"));
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "không tìm thấy seller"));
        if(seller.getUser().getId().equals(currentUser.getId())) {
            OfferRequest offerRequest = new OfferRequest();
            offerRequest.setPostRequest(postRequest);
            offerRequest.setDescriptionBio(request.getDescriptionBio());
            offerRequest.setTotalDeliveryTime(request.getTotalDeliveryTime());
            offerRequest.setCancelFee(request.getCancelFee());
            offerRequest.setOfferPrice(request.getOfferPrice());
            offerRequest.setSeller(seller);
            offerRequest.setOfferType(OfferType.OFFER);
            offerRequest.setOfferRequestStatus(OfferRequestStatus.PENDING);
            OfferRequest save = offerRequestRepository.save(offerRequest);
            String message = "Gửi đi offer thành công qua " + postRequest.getId();
            return new OfferRequestResponse(save.getId(), save.getPostRequest().getId(), save.getDescriptionBio(),
                    save.getTotalDeliveryTime(), save.getOfferPrice(), save.getCancelFee(), message);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    //Not using now
    @Override
    public OfferRequestResponse sendOfferApplyToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy post cần gửi offer"));
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "không tìm thấy seller"));
        if(seller.getUser().getId().equals(currentUser.getId())) {
            OfferRequest offerRequest = new OfferRequest();
            offerRequest.setPostRequest(postRequest);
            offerRequest.setDescriptionBio(request.getDescriptionBio());
            offerRequest.setTotalDeliveryTime(postRequest.getTotalDeliveryTime());
            offerRequest.setCancelFee(postRequest.getContractCancelFee());
            offerRequest.setOfferPrice(postRequest.getBudget());
            offerRequest.setOfferType(OfferType.APPLY);
            offerRequest.setOfferRequestStatus(OfferRequestStatus.PENDING);
            OfferRequest save = offerRequestRepository.save(offerRequest);
            String message = "Gửi đi offer thành công qua " + postRequest.getId();
            return new OfferRequestResponse(save.getId(), save.getPostRequest().getId(), save.getDescriptionBio(),
                    save.getTotalDeliveryTime(), save.getOfferPrice(), save.getCancelFee(), message);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<OfferRequest> getOffers(UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "không tìm thấy seller"));
        List<OfferRequest> offerRequest = offerRequestRepository.findAllBySellerId(seller.getId());
        if(seller.getUser().getId().equals(currentUser.getId())) {
            return offerRequest;
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<OfferRequest> getAllOffersByPostRequest(UUID postRequestId, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findPostRequestById(postRequestId);
        if(postRequest.getUser().getId().equals(currentUser.getId())) {
            return offerRequestRepository.findAllByPostRequestId(postRequest.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
