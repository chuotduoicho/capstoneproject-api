package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.CertificateRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.CertificateResponse;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Box;
import com.jovinn.capstoneproject.model.Certificate;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.CertificateRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CertificateServiceImplTest {
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @Mock
    private SellerRepository sellerRepository;
    @InjectMocks
    private CertificateServiceImpl certificateService;
    private Certificate newCertificate;
    private User newUser;
    private Seller newSeller;
    @BeforeEach
    void setUp() {
        newUser = User.builder()
                .id(UUID.fromString("95f5ed4e-4594-46d9-94b6-8924076283a2"))
                .firstName("Tran")
                .lastName("Son")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();
        newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUser)
                .build();
        newCertificate = Certificate.builder()
                .id(UUID.fromString("6c9ba8dd-0bc7-443d-a3e5-70b84024facb"))
                .title("Math Certificate")
                .name("Cousera of Math")
                .linkCer("www.certificate.com")
                .seller(newSeller)
                .build();
    }
    @DisplayName("JUnit test for addCertificate method")
    @Test
    void givenSellerObjectAndCertificateObject_whenAddCertificate_thenReturnCertificateResponse() {
        // given - precondition or setup
         Certificate newCertificate1 = Certificate.builder()
                .title("Math Certificate1")
                .name("Cousera of Math1")
                .linkCer("www.certificate1.com")
                .seller(newSeller)
                .build();
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setUserId(newUser.getId());
        certificateRequest.setTitle(newCertificate1.getTitle());
        certificateRequest.setName(newCertificate1.getName());
        certificateRequest.setLinkCer(newCertificate1.getLinkCer());
        given(sellerRepository.findSellerByUserId(newUser.getId())).willReturn(Optional.of(newSeller));
        given(certificateRepository.save(newCertificate1)).willReturn(newCertificate1);

        // when -  action or the behaviour that we are going test
        CertificateResponse certificateResponse = certificateService.addCertificate(certificateRequest, UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(certificateResponse).isNotNull();
        assertThat(certificateResponse.getTitle()).isEqualTo(newCertificate1.getTitle());
    }
    @DisplayName("JUnit test for addCertificate method which throw ResourceNotFoundException")
    @Test
    void givenSellerObjectAndCertificateObject_whenAddCertificate_thenThrowException() {
        // given - precondition or setup
        Certificate newCertificate1 = Certificate.builder()
                .title("Math Certificate1")
                .name("Cousera of Math1")
                .linkCer("www.certificate1.com")
                .seller(newSeller)
                .build();
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setUserId(newUser.getId());
        certificateRequest.setTitle(newCertificate1.getTitle());
        certificateRequest.setName(newCertificate1.getName());
        certificateRequest.setLinkCer(newCertificate1.getLinkCer());
        given(sellerRepository.findSellerByUserId(newUser.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            certificateService.addCertificate(certificateRequest, UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(certificateRepository,never()).save(any(Certificate.class));
    }
    @DisplayName("JUnit test for addCertificate method which throw unauthorized exception")
    @Test
    void givenSellerObject_whenSaveBox_thenThrowUnauthorizedException(){
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("5b4ee4c9-9eaa-493f-952b-2bea9e2635cd"))
                .firstName("Tran")
                .lastName("Long")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();
        Certificate newCertificate1 = Certificate.builder()
                .title("Math Certificate1")
                .name("Cousera of Math1")
                .linkCer("www.certificate1.com")
                .seller(newSeller)
                .build();
        given(sellerRepository.findSellerByUserId(newUser1.getId()))
                .willReturn(Optional.of(newSeller));
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setUserId(newUser1.getId());
        certificateRequest.setTitle(newCertificate1.getTitle());
        certificateRequest.setName(newCertificate1.getName());
        certificateRequest.setLinkCer(newCertificate1.getLinkCer());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            certificateService.addCertificate(certificateRequest,UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(certificateRepository,never()).save(any(Certificate.class));
    }
    @DisplayName("JUnit test for updateCertificate method")
    @Test
    void givenSellerObjectAndCertificateObject_whenUpdateCertificate_returnCertificateObject() {
        // given - precondition or setup
        given(sellerRepository.findSellerByUserId(newUser.getId())).willReturn(Optional.of(newSeller));
        given(certificateRepository.findById(newCertificate.getId())).willReturn(Optional.of(newCertificate));
        given(certificateRepository.save(newCertificate)).willReturn(newCertificate);
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setUserId(newUser.getId());
        certificateRequest.setTitle("Cousera of Math1");
        certificateRequest.setName("Math Certificate1");
        certificateRequest.setLinkCer(newCertificate.getLinkCer());
        newCertificate.setName(certificateRequest.getName());
        newCertificate.setTitle(certificateRequest.getTitle());
        // when -  action or the behaviour that we are going test
        CertificateResponse certificateResponse = certificateService.update(newCertificate.getId(),certificateRequest,UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(certificateResponse.getTitle()).isEqualTo("Cousera of Math1");
        assertThat(certificateResponse.getName()).isEqualTo("Math Certificate1");
    }
    @DisplayName("JUnit test for updateCertificate method which throw ResourceNotFoundException exception 1")
    @Test
    void givenSellerEmpty_whenUpdateCertificate_returnThrowException1() {
        // given - precondition or setup
        given(sellerRepository.findSellerByUserId(newUser.getId())).willReturn(Optional.empty());
//        given(certificateRepository.findById(newCertificate.getId())).willReturn(Optional.of(newCertificate));
//        given(certificateRepository.save(newCertificate)).willReturn(newCertificate);
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setUserId(newUser.getId());
        certificateRequest.setTitle(newCertificate.getTitle());
        certificateRequest.setName(newCertificate.getName());
        certificateRequest.setLinkCer(newCertificate.getLinkCer());
//        newCertificate.setName(certificateRequest.getName());
//        newCertificate.setTitle(certificateRequest.getTitle());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            certificateService.update(newCertificate.getId(),certificateRequest,UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(certificateRepository,never()).save(any(Certificate.class));
    }
    @DisplayName("JUnit test for updateCertificate method which throw ResourceNotFoundException exception 2")
    @Test
    void givenCertificateEmpty_whenUpdateCertificate_returnThrowException2() {
        // given - precondition or setup
        given(sellerRepository.findSellerByUserId(newUser.getId())).willReturn(Optional.of(newSeller));
        given(certificateRepository.findById(newCertificate.getId())).willReturn(Optional.empty());
//        given(certificateRepository.save(newCertificate)).willReturn(newCertificate);
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setUserId(newUser.getId());
        certificateRequest.setTitle(newCertificate.getTitle());
        certificateRequest.setName(newCertificate.getName());
        certificateRequest.setLinkCer(newCertificate.getLinkCer());
//        newCertificate.setName(certificateRequest.getName());
//        newCertificate.setTitle(certificateRequest.getTitle());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            certificateService.update(newCertificate.getId(),certificateRequest,UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(certificateRepository,never()).save(any(Certificate.class));
    }
    @DisplayName("JUnit test for updateCertificate method which throw UnauthorizedException exception")
    @Test
    void givenSellerObjectAndCertificateObject_whenUpdateCertificate_returnThrowException3() {
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("5b4ee4c9-9eaa-493f-952b-2bea9e2635cd"))
                .firstName("Tran")
                .lastName("Long")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();
        given(sellerRepository.findSellerByUserId(newUser.getId())).willReturn(Optional.of(newSeller));
        given(certificateRepository.findById(newCertificate.getId())).willReturn(Optional.of(newCertificate));
//        given(certificateRepository.save(newCertificate)).willReturn(newCertificate);
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setUserId(newUser.getId());
        certificateRequest.setTitle(newCertificate.getTitle());
        certificateRequest.setName(newCertificate.getName());
        certificateRequest.setLinkCer(newCertificate.getLinkCer());
//        newCertificate.setName(certificateRequest.getName());
//        newCertificate.setTitle(certificateRequest.getTitle());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            certificateService.update(newCertificate.getId(),certificateRequest,UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(certificateRepository,never()).save(any(Certificate.class));
    }
    @DisplayName("JUnit test for deleteCertificate method")
    @Test
    void givenCertificateObject_whenDeleteCertificate_thenReturnApiResponse() {
        // given - precondition or setup
        given(certificateRepository.findById(newCertificate.getId())).willReturn(Optional.of(newCertificate));
        willDoNothing().given(certificateRepository).deleteById(newCertificate.getId());
        // when -  action or the behaviour that we are going test
        ApiResponse apiResponse = certificateService.delete(newCertificate.getId(),UserPrincipal.create(newUser));
        // then - verify the output
        verify(certificateRepository, times(1)).deleteById(newCertificate.getId());
        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.TRUE, "Certificate deleted successfully"));
    }
    @DisplayName("JUnit test for deleteCertificate method which throw ResourceNotFoundException exception")
    @Test
    void givenCertificateEmpty_whenDeleteCertificate_thenThrowException() {
        // given - precondition or setup
        given(certificateRepository.findById(newCertificate.getId())).willReturn(Optional.empty());
//        willDoNothing().given(certificateRepository).deleteById(newCertificate.getId());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ResourceNotFoundException.class,()->{
            certificateService.delete(newCertificate.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(certificateRepository, times(0)).deleteById(newCertificate.getId());
    }
    @DisplayName("JUnit test for deleteCertificate method which throw UnauthorizedException exception")
    @Test
    void givenCertificateObject_whenDeleteCertificate_thenThrowException() {
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("5b4ee4c9-9eaa-493f-952b-2bea9e2635cd"))
                .firstName("Tran")
                .lastName("Long")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();
        given(certificateRepository.findById(newCertificate.getId())).willReturn(Optional.of(newCertificate));
//        willDoNothing().given(certificateRepository).deleteById(newCertificate.getId());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            certificateService.delete(newCertificate.getId(),UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(certificateRepository, times(0)).deleteById(newCertificate.getId());
    }
}