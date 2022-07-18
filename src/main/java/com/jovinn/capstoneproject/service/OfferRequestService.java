package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.OfferRequestRequest;
import com.jovinn.capstoneproject.dto.response.OfferRequestResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface OfferRequestService {
    OfferRequestResponse sendOfferToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser);
    OfferRequestResponse sendOfferApplyToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser);
}
