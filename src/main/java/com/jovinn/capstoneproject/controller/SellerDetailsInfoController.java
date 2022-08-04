package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.client.request.*;
import com.jovinn.capstoneproject.dto.client.response.*;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller-details")
@CrossOrigin(origins = "*")
public class SellerDetailsInfoController {
    @Autowired
    private CertificateService certificateService;
    @Autowired
    private EducationService educationService;
    @Autowired
    private LanguageService languageService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private UrlProfileService urlProfileService;

    @PostMapping("/certificate")
    public ResponseEntity<CertificateResponse> addCertificate(@Valid @RequestBody CertificateRequest request,
                                                              @CurrentUser UserPrincipal currentUser) {
        CertificateResponse certificateResponse = certificateService.addCertificate(request, currentUser);
        return new ResponseEntity< >(certificateResponse, HttpStatus.OK);
    }

    @PutMapping("/certificate/{id}")
    public ResponseEntity<CertificateResponse> updateCertificate(@PathVariable(name = "id") UUID id,
                                                                 @Valid @RequestBody CertificateRequest request,
                                                                 @CurrentUser UserPrincipal currentUser) {
        CertificateResponse response = certificateService.update(id, request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @DeleteMapping("/certificate/{id}")
    public ResponseEntity<ApiResponse> deleteCertificate(@PathVariable(name = "id") UUID id,
                                                         @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = certificateService.delete(id, currentUser);
        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/education")
    public ResponseEntity<EducationResponse> addEducation(@Valid @RequestBody EducationRequest educationRequest,
                                                          @CurrentUser UserPrincipal currentUser) {
        EducationResponse educationResponse = educationService.addEducation(educationRequest, currentUser);
        return new ResponseEntity< >(educationResponse, HttpStatus.OK);
    }

    @PutMapping("/education/{id}")
    public ResponseEntity<EducationResponse> updateEducation(@PathVariable(name = "id") UUID id,
                                                             @Valid @RequestBody EducationRequest request,
                                                             @CurrentUser UserPrincipal currentUser) {
        EducationResponse response = educationService.update(id, request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<ApiResponse> deleteEducation(@PathVariable(name = "id") UUID id,
                                                       @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = educationService.delete(id, currentUser);
        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/language")
    public ResponseEntity<LanguageResponse> addLanguage(@Valid @RequestBody LanguageRequest languageRequest,
                                                        @CurrentUser UserPrincipal currentUser) {
        LanguageResponse response = languageService.addLanguage(languageRequest, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @PutMapping("/language/{id}")
    public ResponseEntity<LanguageResponse> updateLanguage(@PathVariable(name = "id") UUID id,
                                                           @Valid @RequestBody LanguageRequest request,
                                                           @CurrentUser UserPrincipal currentUser) {
        LanguageResponse response = languageService.update(id, request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @DeleteMapping("/language/{id}")
    public ResponseEntity<ApiResponse> deleteLanguage(@PathVariable(name = "id") UUID id,
                                                      @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = languageService.delete(id, currentUser);
        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/skill")
    public ResponseEntity<SkillResponse> addSkill(@Valid @RequestBody SkillRequest request,
                                                  @CurrentUser UserPrincipal currentUser) {
        SkillResponse response = skillService.addSkill(request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @PutMapping("/skill/{id}")
    public ResponseEntity<SkillResponse> updateSkill(@PathVariable(name = "id") UUID id,
                                                     @Valid @RequestBody SkillRequest request,
                                                     @CurrentUser UserPrincipal currentUser) {
        SkillResponse response = skillService.update(id, request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @DeleteMapping("/skill/{id}")
    public ResponseEntity<ApiResponse> deleteSkill(@PathVariable(name = "id") UUID id,
                                                   @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = skillService.delete(id, currentUser);
        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/url-profile")
    public ResponseEntity<UrlProfileResponse> addUrlProfile(@Valid @RequestBody UrlProfileRequest request,
                                                            @CurrentUser UserPrincipal currentUser) {
        UrlProfileResponse response = urlProfileService.addUrl(request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @PutMapping("/url-profile/{id}")
    public ResponseEntity<UrlProfileResponse> updateUrlProfile(@PathVariable(name = "id") UUID id,
                                                               @Valid @RequestBody UrlProfileRequest request,
                                                               @CurrentUser UserPrincipal currentUser) {
        UrlProfileResponse response = urlProfileService.update(id, request, currentUser);
        return new ResponseEntity< >(response, HttpStatus.OK);
    }

    @DeleteMapping("/url-profile/{id}")
    public ResponseEntity<ApiResponse> deleteUrlProfile(@PathVariable(name = "id") UUID id,
                                                        @CurrentUser UserPrincipal currentUser) {
        ApiResponse apiResponse = urlProfileService.delete(id, currentUser);
        return new ResponseEntity< >(apiResponse, HttpStatus.OK);
    }
}
