package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.UrlProfileRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.UrlProfileResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface UrlProfileService {
    UrlProfileResponse addUrl(UrlProfileRequest request, UserPrincipal currentUser);
    UrlProfileResponse update(UUID id, UrlProfileRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
