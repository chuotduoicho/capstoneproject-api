package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.request.RatingRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.RatingResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private BoxRepository boxRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public ApiResponse ratingSeller(UUID contractId, RatingRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy người mua"));
        Seller seller = sellerRepository.findById(contract.getSeller().getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy người bán"));
        String boxId = packageRepository.getBoxIdByPackage(contract.getPackageId());
        Box box = boxRepository.findById(UUID.fromString(boxId))
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy hộp dịch vụ"));
        if (contract.getBuyer().getUser().getId().equals(currentUser.getId()) &&
                contract.getContractStatus().equals(ContractStatus.COMPLETE)) {
                Rating rating = new Rating();
                rating.setBuyerId(buyer.getId());
                rating.setSellerId(contract.getSeller().getId());
                rating.setBox(box);
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
    public List<RatingResponse> getTop3Ratings(UUID boxId) {
        List<Rating> ratings = ratingRepository.findAllByBoxIdOrderByRatingPointDesc(boxId, PageRequest.of(0, 3));
        return ratings.stream().map(
                rating -> modelMapper.map(rating, RatingResponse.class))
                .collect(Collectors.toList());
//        Pageable pageable = Pagination.paginationCommon(page, size, "createAt", "desc");
//        Page<Rating> ratings = ratingRepository.findAllBySellerId(sellerId, pageable);
//        List<Rating> content = ratings.getNumberOfElements() == 0 ? Collections.emptyList() : ratings.getContent();
//        return new PageResponse<>(content, ratings.getNumber(), ratings.getSize(), ratings.getTotalElements(),
//                ratings.getTotalPages(), ratings.isLast());
    }

    @Override
    public List<RatingResponse> getRatingsForBox(UUID boxId, int page) {
        List<Rating> ratings = ratingRepository.findAllByBoxIdOrderByCreateAtDesc(boxId, PageRequest.of(page, 5));

        List<RatingResponse> responses = new ArrayList<>();
        for(Rating rating : ratings) {
            Buyer buyer = buyerRepository.findById(rating.getBuyerId())
                    .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy người bán"));
            responses.add(new RatingResponse(rating.getId(), rating.getCreateAt(), buyer.getUser().getAvatar(),
                    buyer.getUser().getLastName() + " " + buyer.getUser().getFirstName(),
                    rating.getRatingPoint(), rating.getComment(), ratings.size()));
        }
        return responses;
//        Pageable pageable = Pagination.paginationCommon(page, size, "createAt", "desc");
//        Page<Rating> ratings = ratingRepository.findAllByContractId(contractId, pageable);
//        List<Rating> content = ratings.getNumberOfElements() == 0 ? Collections.emptyList() : ratings.getContent();
//        return new PageResponse<>(content, ratings.getNumber(), ratings.getSize(), ratings.getTotalElements(),
//                ratings.getTotalPages(), ratings.isLast());
    }
}
