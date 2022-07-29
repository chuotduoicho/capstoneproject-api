package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.request.RatingRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.model.Rating;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface RatingService {
    ApiResponse ratingSeller(UUID contractId, RatingRequest request, UserPrincipal currentUser);
    PageResponse<Rating> getRatingsForSeller(UUID sellerId, int page, int size);
    PageResponse<Rating> getRatingsForContract(UUID contractId, int page, int size);
}
