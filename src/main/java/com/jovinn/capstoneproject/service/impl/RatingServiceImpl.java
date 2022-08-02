package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.request.RatingRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Buyer;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.Rating;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.BuyerRepository;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.repository.RatingRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.RatingService;
import com.jovinn.capstoneproject.util.Pagination;
import com.jovinn.capstoneproject.util.ValidInputRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.jovinn.capstoneproject.util.ValidInputRating.getInputRating;

@Service
public class RatingServiceImpl implements RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Override
    public ApiResponse ratingSeller(UUID contractId, RatingRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found"));
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy buyer"));
        Seller seller = sellerRepository.findById(contract.getSeller().getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy seller"));
        if (contract.getBuyer().getUser().getId().equals(currentUser.getId()) &&
                contract.getContractStatus().equals(ContractStatus.COMPLETE)) {
                Rating rating = new Rating();
                rating.setBuyerId(buyer.getId());
                rating.setContractId(contract.getId());
                rating.setSeller(contract.getSeller());
                rating.setComment(request.getComment());
                rating.setRatingPoint(getInputRating(request.getRatingPoint()));
                ratingRepository.save(rating);
                seller.setRatingPoint((seller.getRatingPoint() + rating.getRatingPoint())/2);
                sellerRepository.save(seller);
            return new ApiResponse(Boolean.TRUE, "Bạn đã thực hiện đánh giá thành công");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<Rating> getRatingsForSeller(UUID sellerId) {
        return ratingRepository.findAllBySellerId(sellerId);
//        Pageable pageable = Pagination.paginationCommon(page, size, "createAt", "desc");
//        Page<Rating> ratings = ratingRepository.findAllBySellerId(sellerId, pageable);
//        List<Rating> content = ratings.getNumberOfElements() == 0 ? Collections.emptyList() : ratings.getContent();
//        return new PageResponse<>(content, ratings.getNumber(), ratings.getSize(), ratings.getTotalElements(),
//                ratings.getTotalPages(), ratings.isLast());
    }

    @Override
    public List<Rating> getRatingsForContract(UUID contractId) {
        return ratingRepository.findAllByContractId(contractId);
//        Pageable pageable = Pagination.paginationCommon(page, size, "createAt", "desc");
//        Page<Rating> ratings = ratingRepository.findAllByContractId(contractId, pageable);
//        List<Rating> content = ratings.getNumberOfElements() == 0 ? Collections.emptyList() : ratings.getContent();
//        return new PageResponse<>(content, ratings.getNumber(), ratings.getSize(), ratings.getTotalElements(),
//                ratings.getTotalPages(), ratings.isLast());
    }
}
