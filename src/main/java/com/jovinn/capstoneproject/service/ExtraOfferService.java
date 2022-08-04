package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.ExtraOfferRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface ExtraOfferService {
    ApiResponse createExtraOffer(UUID contractId, ExtraOfferRequest request, UserPrincipal currentUser);
    ApiResponse sellerAcceptExtraOffer(UUID contractId, UUID extraOfferId, UserPrincipal currentUser);
}
