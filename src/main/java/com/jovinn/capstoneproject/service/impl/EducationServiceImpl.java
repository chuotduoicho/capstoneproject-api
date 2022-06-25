package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.EducationRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.EducationResponse;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Education;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.EducationRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class EducationServiceImpl implements EducationService {
    @Autowired
    private EducationRepository educationRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Override
    public EducationResponse addEducation(EducationRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", request.getUserId()));
        if(seller.getUser().getId().equals(currentUser.getId())) {
            Education education = new Education(request.getCountry(),
                    request.getUniversityName(),
                    request.getTitle(), request.getMajor(),
                    request.getYearOfGraduation(), request.getFromDate(),
                    request.getToDate(), request.getOpened(), seller);
            Education newEdu = educationRepository.save(education);
            return new EducationResponse(newEdu.getId(), newEdu.getCountry(),
                    newEdu.getUniversityName(),
                    newEdu.getTitle(), newEdu.getMajor(),
                    newEdu.getYearOfGraduation(), newEdu.getFromDate(),
                    newEdu.getToDate(), newEdu.getOpened(), newEdu.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public EducationResponse update(UUID id, EducationRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", request.getUserId()));
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Education not found", request.getUserId()));
        if (education.getSeller().getUser().getId().equals(currentUser.getId())) {
           education.setCountry(request.getCountry());
           education.setUniversityName(request.getUniversityName());
           education.setTitle(request.getTitle());
           education.setMajor(request.getMajor());
           education.setYearOfGraduation(request.getYearOfGraduation());
           education.setFromDate(request.getFromDate());
           education.setToDate(request.getToDate());
           education.setOpened(request.getOpened());
           education.setUpdatedAt(new Date());
           education.setSeller(seller);
           Education update = educationRepository.save(education);
           return new EducationResponse(update.getId(), update.getCountry(),
                    update.getUniversityName(),
                    update.getTitle(), update.getMajor(),
                    update.getYearOfGraduation(), update.getFromDate(),
                    update.getToDate(), update.getOpened(), update.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(UUID id, UserPrincipal currentUser) {
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Education", "Education Not Found", id));
        if (education.getSeller().getUser().getId().equals(currentUser.getId())) {
            educationRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Education deleted successfully");
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }
}
