package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.request.OfferRequestRequest;
import com.jovinn.capstoneproject.dto.response.OfferRequestResponse;
import com.jovinn.capstoneproject.model.OfferRequest;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface OfferRequestService {
    OfferRequestResponse sendOfferToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser);
    OfferRequestResponse sendOfferApplyToBuyer(UUID postRequestId, OfferRequestRequest request, UserPrincipal currentUser);
    PageResponse<OfferRequest> getOffers(UserPrincipal currentUser, int page, int size, String sortBy, String sortDir);
    PageResponse<OfferRequest> getAllOffersByPostRequest(UUID postRequestId, UserPrincipal currentUser, int page, int size, String sortBy, String sortDir);
}
