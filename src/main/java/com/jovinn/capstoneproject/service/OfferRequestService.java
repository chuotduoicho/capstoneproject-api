package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.OfferRequestRequest;
import com.jovinn.capstoneproject.dto.client.response.OfferRequestResponse;
import com.jovinn.capstoneproject.model.OfferRequest;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.List;
import java.util.UUID;

public interface OfferRequestService {
    OfferRequestResponse sendOfferToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser);
    OfferRequestResponse sendOfferApplyToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser);
    List<OfferRequest> getOffers(UserPrincipal currentUser);
    List<OfferRequest> getAllOffersByPostRequest(UUID postRequestId, UserPrincipal currentUser);
}
