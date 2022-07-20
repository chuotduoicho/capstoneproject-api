package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.RatingRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.model.Rating;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface RatingService {
    ApiResponse ratingSeller(UUID contractId, RatingRequest request, UserPrincipal currentUser);
    List<Rating> getRatingsForSeller(UUID sellerId);
    List<Rating> getRatingsForContract(UUID contractId);
}
