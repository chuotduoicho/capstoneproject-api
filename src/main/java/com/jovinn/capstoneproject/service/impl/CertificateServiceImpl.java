package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.CertificateRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.CertificateResponse;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Certificate;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.CertificateRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public CertificateResponse addCertificate(CertificateRequest certificateRequest, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(certificateRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", certificateRequest.getUserId()));
        if (seller.getUser().getId().equals(currentUser.getId())) {
            Certificate certificate = new Certificate(certificateRequest.getTitle(),
                    certificateRequest.getName(),
                    certificateRequest.getLinkCer(), seller);
            Certificate newCer = certificateRepository.save(certificate);
            return new CertificateResponse(newCer.getId(), newCer.getTitle(),
                    newCer.getName(), newCer.getLinkCer(), newCer.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public CertificateResponse update(UUID id, CertificateRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", request.getUserId()));
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "Certificate Not Found", request.getUserId()));
        if (certificate.getSeller().getUser().getId().equals(currentUser.getId())) {
           certificate.setTitle(request.getTitle());
           certificate.setName(request.getName());
           certificate.setLinkCer(request.getLinkCer());
           certificate.setUpdatedAt(new Date());
           certificate.setSeller(seller);
           Certificate update = certificateRepository.save(certificate);
           return new CertificateResponse(update.getId(), update.getTitle(),
                    update.getName(), update.getLinkCer(), update.getSeller().getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to add certificate with seller");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse delete(UUID id, UserPrincipal currentUser) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "Certificate Not Found", id));
        if (certificate.getSeller().getUser().getId().equals(currentUser.getId())) {
            certificateRepository.deleteById(id);
            return new ApiResponse(Boolean.TRUE, "Certificate deleted successfully");
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to delete this photo");
        throw new UnauthorizedException(apiResponse);
    }
}
