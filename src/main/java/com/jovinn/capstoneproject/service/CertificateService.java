package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.CertificateRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.CertificateResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface CertificateService {
    CertificateResponse addCertificate(CertificateRequest certificateRequest, UserPrincipal currentUser);
    CertificateResponse update(UUID id, CertificateRequest request, UserPrincipal currentUser);
    ApiResponse delete(UUID id, UserPrincipal currentUser);
}
