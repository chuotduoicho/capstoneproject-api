package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.LanguageRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.LanguageResponse;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Language;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.LanguageRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class LanguageServiceImpl implements LanguageService {
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public LanguageResponse addLanguage(LanguageRequest languageRequest, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(languageRequest.getUserId())
                .orElseThrow(() ->
                        new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy "));
        if(seller.getUser().getId().equals(currentUser.getId())) {
            Language language = new Language(languageRequest.getLanguage(), seller);
            Language newLang = languageRepository.save(language);
            return new LanguageResponse(newLang.getId(), newLang.getLanguage(),
                    newLang.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public LanguageResponse update(UUID id, LanguageRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Seller not found", request.getUserId()));
        Language language = languageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller", "Seller not found", request.getUserId()));
        if(language.getSeller().getUser().getId().equals(currentUser.getId())) {
            language.setLanguage(request.getLanguage());
            language.setUpdatedAt(new Date());
            language.setSeller(seller);
            Language update = languageRepository.save(language);
            return new LanguageResponse(update.getId(), update.getLanguage(),
                    update.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(UUID id, UserPrincipal currentUser) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Language", "Language Not Found", id));
        if (language.getSeller().getUser().getId().equals(currentUser.getId())) {
            languageRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Language deleted successfully");
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
