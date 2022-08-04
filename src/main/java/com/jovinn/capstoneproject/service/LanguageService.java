package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.LanguageRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.LanguageResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface LanguageService {
    LanguageResponse addLanguage(LanguageRequest languageRequest, UserPrincipal currentUser);
    LanguageResponse update(UUID id, LanguageRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
