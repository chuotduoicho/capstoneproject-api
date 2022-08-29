package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.OfferRequestRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.OfferRequestResponse;
import com.jovinn.capstoneproject.enumerable.OfferRequestStatus;
import com.jovinn.capstoneproject.enumerable.OfferType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.OfferRequest;
import com.jovinn.capstoneproject.model.PostRequest;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Wallet;
import com.jovinn.capstoneproject.repository.OfferRequestRepository;
import com.jovinn.capstoneproject.repository.PostRequestRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.OfferRequestService;
import com.jovinn.capstoneproject.util.PushNotification;
import com.jovinn.capstoneproject.util.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private PushNotification pushNotification;
    @Override
    public OfferRequestResponse sendOfferToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy post cần gửi đề nghị"));
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "không tìm thấy người bán"));
        Wallet walletSeller = walletRepository.findWalletByUserId(currentUser.getId());

        if(seller.getUser().getId().equals(currentUser.getId())) {
            BigDecimal serviceDeposit = request.getOfferPrice()
                    .multiply(new BigDecimal(request.getCancelFee()))
                            .divide(new BigDecimal(100), RoundingMode.FLOOR);
            if(walletSeller.getWithdraw().compareTo(serviceDeposit) >= 0) {
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
                pushNotification.sendNotification(postRequest.getUser(), WebConstant.DOMAIN + "/buyerHome/manageOffer/" + postRequest.getId(),
                        "Bạn nhận được lời đề nghị mới từ " + seller.getBrandName());
                String message = "Gửi đi dề nghị thành công qua " + postRequest.getId();
                return new OfferRequestResponse(save.getId(), save.getPostRequest().getId(), save.getDescriptionBio(),
                        save.getTotalDeliveryTime(), save.getOfferPrice(), save.getCancelFee(), message);
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn cần có một lượng cọc nhất định trong tài khoản, vui lòng nạp thêm");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    //Not using now
    @Override
    public OfferRequestResponse sendOfferApplyToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy bài đăng cần gửi đề nghị"));
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "không tìm thấy người bán"));
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
            String message = "Gửi đi đề nghị thành công cho " + postRequest.getId();
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
        //Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);

        //List<OfferRequest> offerRequest = offerRequestRepository.findAllBySellerId(seller.getId());
        if(seller.getUser().getId().equals(currentUser.getId())) {
//            Page<OfferRequest> offerRequests = offerRequestRepository.findAllBySellerId(seller.getId(), pageable);
//            List<OfferRequest> content = offerRequests.getNumberOfElements()  == 0 ? Collections.emptyList() : offerRequests.getContent();
//            return new PageResponse<>(content, offerRequests.getNumber(), offerRequests.getSize(), offerRequests.getTotalElements(),
//                    offerRequests.getTotalPages(), offerRequests.isLast());
            return offerRequestRepository.findAllBySellerId(seller.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<OfferRequest> getAllOffersByPostRequest(UUID postRequestId, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tim thấy bài đăng"));
        //Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);

        if(postRequest.getUser().getId().equals(currentUser.getId())) {
//            Page<OfferRequest> offerRequests = offerRequestRepository.findAllByPostRequestId(postRequest.getId(), pageable);
//            List<OfferRequest> content = offerRequests.getNumberOfElements()  == 0 ? Collections.emptyList() : offerRequests.getContent();
//            return new PageResponse<>(content, offerRequests.getNumber(), offerRequests.getSize(), offerRequests.getTotalElements(),
//                    offerRequests.getTotalPages(), offerRequests.isLast());
            return offerRequestRepository.findAllByPostRequestId(postRequest.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
