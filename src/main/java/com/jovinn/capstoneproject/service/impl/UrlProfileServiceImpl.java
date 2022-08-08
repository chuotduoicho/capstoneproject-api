package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.UrlProfileRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.UrlProfileResponse;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.UrlProfile;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.UrlProfileRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.UrlProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class UrlProfileServiceImpl implements UrlProfileService {
    @Autowired
    private UrlProfileRepository urlProfileRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public UrlProfileResponse addUrl(UrlProfileRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Seller not found", request.getUserId()));
        if (seller.getUser().getId().equals(currentUser.getId())) {
            UrlProfile urlProfile = new UrlProfile(request.getTitle(), request.getUrl(), seller);
            UrlProfile newUrl = urlProfileRepository.save(urlProfile);
            return new UrlProfileResponse(newUrl.getId(),
                    newUrl.getTitle(), newUrl.getUrl(),
                    newUrl.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public UrlProfileResponse update(UUID id, UrlProfileRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Seller not found", request.getUserId()));
        UrlProfile urlProfile = urlProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Url not found", id));
        if (urlProfile.getSeller().getUser().getId().equals(currentUser.getId())) {
            urlProfile.setTitle(request.getTitle());
            urlProfile.setUrl(request.getTitle());
            urlProfile.setUpdatedAt(new Date());
            urlProfile.setSeller(seller);
            UrlProfile newUrl = urlProfileRepository.save(urlProfile);
            return new UrlProfileResponse(newUrl.getId(),
                    newUrl.getTitle(), newUrl.getUrl(),
                    newUrl.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(UUID id, UserPrincipal currentUser) {
        UrlProfile urlProfile = urlProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UrlProfile", "UrlProfile Not Found", id));
        if (urlProfile.getSeller().getUser().getId().equals(currentUser.getId())) {
            urlProfileRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "UrlProfile deleted successfully");
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }
}
