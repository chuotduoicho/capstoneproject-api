package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.RatingRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.RatingResponse;
import com.jovinn.capstoneproject.model.Rating;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface RatingService {
    ApiResponse ratingSeller(UUID contractId, RatingRequest request, UserPrincipal currentUser);
    List<RatingResponse> getTop3Ratings(UUID boxId);
    List<RatingResponse> getRatingsForBox(UUID boxId, int page);
}
