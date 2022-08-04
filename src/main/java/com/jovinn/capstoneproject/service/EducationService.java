package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.EducationRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.EducationResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface EducationService {
    EducationResponse addEducation(EducationRequest request, UserPrincipal currentUser);
    EducationResponse update(UUID id, EducationRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
