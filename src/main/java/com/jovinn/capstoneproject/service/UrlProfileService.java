package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.UrlProfileRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.UrlProfileResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface UrlProfileService {
    UrlProfileResponse addUrl(UrlProfileRequest request, UserPrincipal currentUser);
    UrlProfileResponse update(UUID id, UrlProfileRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
