package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.OfferRequestRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.OfferRequestResponse;
import com.jovinn.capstoneproject.enumerable.*;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.repository.OfferRequestRepository;
import com.jovinn.capstoneproject.repository.PostRequestRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
@ExtendWith(MockitoExtension.class)
class OfferRequestServiceImplTest {
    @Mock
    private OfferRequestRepository offerRequestRepository;
    @Mock
    private WalletRepository walletRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private PostRequestRepository postRequestRepository;
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @InjectMocks
    private OfferRequestServiceImpl offerRequestService;
    private PostRequest newPostRequest;
    private Category newCategory;
    private SubCategory newSubCategory;
    private Skill newSkill;
    private User newUser;
    private MilestoneContract newMilestoneContract;
    @BeforeEach
    void setUp() {
        newSkill = Skill.builder()
                .id(UUID.fromString("b1560615-9e1c-497d-93e3-94265aee64db"))
                .name("code")
                .build();
        newUser = User.builder()
                .id(UUID.fromString("95f5ed4e-4594-46d9-94b6-8924076283a2"))
                .firstName("Tran")
                .lastName("Son")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.BUYER))
                .isEnabled(true)
                .city("Thai Nguyen")
                .buyer(mock(Buyer.class,RETURNS_DEEP_STUBS))
                .build();
        newCategory = Category.builder()
                .id(UUID.fromString("66c2360c-01f4-483d-a404-e5f0a6818bd9"))
                .name("Web")
                .build();
        newSubCategory = SubCategory.builder()
                .id(UUID.fromString("2ef78cde-5a54-4b16-bb2c-3f65656ed200"))
                .name("Web backend")
                .category(newCategory)
                .build();
        newMilestoneContract = MilestoneContract.builder()
                .id(UUID.fromString("e884ea3c-21f6-4e02-a432-ffa0ea8fa82b"))
                .description("description 1")
                .startDate(new Date(2020-7-7))
                .endDate(new Date(2020-8-8))
                .status(MilestoneStatus.PROCESSING)
                .postRequest(newPostRequest)
                .milestoneFee(new BigDecimal(1))
                .build();
        newPostRequest = PostRequest.builder()
                .id(UUID.fromString("0dbe45fc-b8ac-497c-8e42-341f664a0a5b"))
                .status(PostRequestStatus.OPEN)
                .recruitLevel("recruitLevel1")
                .jobTitle("jobTitle1")
                .shortRequirement("shortRequirement1")
                .attachFile("attachFile1")
                .contractCancelFee(1)
                .totalDeliveryTime(1)
                .budget(new BigDecimal(1))
                .category(newCategory)
                .subCategory(newSubCategory)
                .skills(List.of(newSkill))
                .user(newUser)
                .sellersApplyRequest(List.of())
                .milestoneContracts(List.of(newMilestoneContract))
                .build();
    }
    @DisplayName("Junit test for sendOfferToBuyer method")
    @Test
    void givenData_whenSendOfferToBuyer_thenReturnOfferRequestResponse() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        Seller newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .build();
        Wallet newWalletSeller = Wallet.builder()
                .id(UUID.fromString("6ebbfc46-6cf7-49aa-905e-cc47a21437c2"))
                .income(new BigDecimal(1000))
                .withdraw(new BigDecimal(100))
                .user(newUserSeller)
                .build();

        OfferRequestRequest offerRequestRequest = new OfferRequestRequest();
        offerRequestRequest.setDescriptionBio("description bio");
        offerRequestRequest.setTotalDeliveryTime(1);
        offerRequestRequest.setOfferPrice(new BigDecimal(10));
        offerRequestRequest.setCancelFee(5);
        OfferRequest offerRequest = OfferRequest.builder()
                .id(UUID.fromString("bb051b0b-fe36-4d43-834e-decbe3bc9c24"))
                .postRequest(newPostRequest)
                .descriptionBio(offerRequestRequest.getDescriptionBio())
                .totalDeliveryTime(offerRequestRequest.getTotalDeliveryTime())
                .cancelFee(offerRequestRequest.getCancelFee())
                .offerPrice(offerRequestRequest.getOfferPrice())
                .seller(newSeller)
                .offerType(OfferType.OFFER)
                .offerRequestStatus(OfferRequestStatus.PENDING)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
        given(walletRepository.findWalletByUserId(newUserSeller.getId())).willReturn(newWalletSeller);
        given(offerRequestRepository.save(offerRequest)).willReturn(offerRequest);
        // when -  action or the behaviour that we are going test
        OfferRequestResponse offerRequestResponse = offerRequestService.sendOfferToBuyer(newPostRequest.getId(),offerRequestRequest, UserPrincipal.create(newUserSeller));
        // then - verify the output
        assertThat(offerRequestResponse).isNotNull();
        assertThat(offerRequestResponse.getMessage()).isEqualTo("Gửi đi offer thành công qua " + newPostRequest.getId());
    }

    @DisplayName("Junit test for sendOfferToBuyer method which throw JovinnException")
    @Test
    void givenData_whenSendOfferToBuyer_thenThrowJovinnException() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        Seller newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .build();
        Wallet newWalletSeller = Wallet.builder()
                .id(UUID.fromString("6ebbfc46-6cf7-49aa-905e-cc47a21437c2"))
                .income(new BigDecimal(1000))
                .withdraw(new BigDecimal(1))
                .user(newUserSeller)
                .build();
        OfferRequestRequest offerRequestRequest = new OfferRequestRequest();
        offerRequestRequest.setDescriptionBio("description bio");
        offerRequestRequest.setTotalDeliveryTime(1);
        offerRequestRequest.setOfferPrice(new BigDecimal(1000));
        offerRequestRequest.setCancelFee(50);
        OfferRequest offerRequest = OfferRequest.builder()
                .id(UUID.fromString("bb051b0b-fe36-4d43-834e-decbe3bc9c24"))
                .postRequest(newPostRequest)
                .descriptionBio(offerRequestRequest.getDescriptionBio())
                .totalDeliveryTime(offerRequestRequest.getTotalDeliveryTime())
                .cancelFee(offerRequestRequest.getCancelFee())
                .offerPrice(offerRequestRequest.getOfferPrice())
                .seller(newSeller)
                .offerType(OfferType.OFFER)
                .offerRequestStatus(OfferRequestStatus.PENDING)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
        given(walletRepository.findWalletByUserId(newUserSeller.getId())).willReturn(newWalletSeller);
        // when -  action or the behaviour that we are going test
        JovinnException thrown = assertThrows(JovinnException.class,()->{
            offerRequestService.sendOfferToBuyer(newPostRequest.getId(),offerRequestRequest, UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        assertTrue(thrown.getMessage().contains("Bạn cần có một lượng cọc nhất định trong tài khoản, vui lòng nạp thêm"));

    }
    @DisplayName("Junit test for sendOfferToBuyer method which throw ApiException 1")
    @Test
    void givenEmptyPostRequest_whenSendOfferToBuyer_thenThrowApiException1() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.empty());
        OfferRequestRequest offerRequestRequest = new OfferRequestRequest();
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            offerRequestService.sendOfferToBuyer(newPostRequest.getId(),offerRequestRequest, UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("Không tìm thấy post cần gửi offer");
    }

    @DisplayName("Junit test for sendOfferToBuyer method which throw ApiException 2")
    @Test
    void givenEmptyPostRequest_whenSendOfferToBuyer_thenThrowApiException2() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.empty());
        OfferRequestRequest offerRequestRequest = new OfferRequestRequest();
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            offerRequestService.sendOfferToBuyer(newPostRequest.getId(),offerRequestRequest, UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("không tìm thấy seller");
    }
    @DisplayName("Junit test for sendOfferApplyToBuyer method")
    @Test
    void giveData_whenSendOfferApplyToBuyer_thenReturnOfferRequestResponse() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        Seller newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .build();
        OfferRequestRequest offerRequestRequest = new OfferRequestRequest();
        offerRequestRequest.setDescriptionBio("description bio");
        offerRequestRequest.setTotalDeliveryTime(1);
        offerRequestRequest.setOfferPrice(new BigDecimal(1000));
        offerRequestRequest.setCancelFee(50);
        OfferRequest offerRequest = OfferRequest.builder()
                .id(UUID.fromString("bb051b0b-fe36-4d43-834e-decbe3bc9c24"))
                .postRequest(newPostRequest)
                .descriptionBio(offerRequestRequest.getDescriptionBio())
                .totalDeliveryTime(offerRequestRequest.getTotalDeliveryTime())
                .cancelFee(offerRequestRequest.getCancelFee())
                .offerPrice(offerRequestRequest.getOfferPrice())
                .seller(newSeller)
                .offerType(OfferType.APPLY)
                .offerRequestStatus(OfferRequestStatus.PENDING)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
        given(offerRequestRepository.save(offerRequest)).willReturn(offerRequest);
        // when -  action or the behaviour that we are going test
        OfferRequestResponse offerRequestResponse = offerRequestService.sendOfferApplyToBuyer(newPostRequest.getId(),offerRequestRequest,UserPrincipal.create(newUserSeller));
        // then - verify the output
        assertThat(offerRequestResponse.getMessage()).isEqualTo("Gửi đi offer thành công qua " + newPostRequest.getId());
    }

    @DisplayName("Junit test for sendOfferApplyToBuyer method ApiException 1")
    @Test
    void giveEmptyPostRequest_whenSendOfferApplyToBuyer_thenThrowApiException1() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.empty());
        OfferRequestRequest offerRequestRequest = new OfferRequestRequest();
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            offerRequestService.sendOfferApplyToBuyer(newPostRequest.getId(),offerRequestRequest, UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("Không tìm thấy post cần gửi offer");

    }

    @DisplayName("Junit test for sendOfferApplyToBuyer method ApiException 2")
    @Test
    void giveEmptyPostRequest_whenSendOfferApplyToBuyer_thenThrowApiException2() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.empty());
        OfferRequestRequest offerRequestRequest = new OfferRequestRequest();
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            offerRequestService.sendOfferApplyToBuyer(newPostRequest.getId(),offerRequestRequest, UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("không tìm thấy seller");
    }
    @DisplayName("Junit test for getOffers method")
    @Test
    void givenData_whenGetOffers_thenReturnListOfferRequest() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        Seller newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .build();
        OfferRequest offerRequest = OfferRequest.builder()
                .id(UUID.fromString("bb051b0b-fe36-4d43-834e-decbe3bc9c24"))
                .postRequest(newPostRequest)
                .descriptionBio("description bio")
                .totalDeliveryTime(1)
                .cancelFee(10)
                .offerPrice(new BigDecimal(100))
                .seller(newSeller)
                .offerType(OfferType.APPLY)
                .offerRequestStatus(OfferRequestStatus.PENDING)
                .build();
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
        given(offerRequestRepository.findAllBySellerId(newSeller.getId())).willReturn(List.of(offerRequest));
        // when -  action or the behaviour that we are going test
        List<OfferRequest> offerRequests = offerRequestService.getOffers(UserPrincipal.create(newUserSeller));
        // then - verify the output
        assertThat(offerRequests).isNotNull();
        assertThat(offerRequests.size()).isEqualTo(1);
    }

    @DisplayName("Junit test for getOffers method which throw ApiException")
    @Test
    void givenData_whenGetOffers_thenThrowApiException() {
        // given - precondition or setup
        User newUserSeller = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .isEnabled(true)
                .build();
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            offerRequestService.getOffers(UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("không tìm thấy seller");
    }
    @DisplayName("Junit test for getAllOffersByPostRequest method ")
    @Test
    void givenPostRequestObjectAndListOfferRequest_whenGetAllOffersByPostRequest_thenReturnListOfferRequest() {
        // given - precondition or setup
        OfferRequest offerRequest = OfferRequest.builder()
                .id(UUID.fromString("bb051b0b-fe36-4d43-834e-decbe3bc9c24"))
                .postRequest(newPostRequest)
                .descriptionBio("description bio")
                .totalDeliveryTime(1)
                .cancelFee(10)
                .offerPrice(new BigDecimal(100))
//                .seller(newSeller)
                .offerType(OfferType.APPLY)
                .offerRequestStatus(OfferRequestStatus.PENDING)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(offerRequestRepository.findAllByPostRequestId(newPostRequest.getId())).willReturn(List.of(offerRequest));
        // when -  action or the behaviour that we are going test
        List<OfferRequest> offerRequests = offerRequestService.getAllOffersByPostRequest(newPostRequest.getId(),UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(offerRequests).isNotNull();
        assertThat(offerRequests.size()).isEqualTo(1);
    }

    @DisplayName("Junit test for getAllOffersByPostRequest method which throw JovinnException")
    @Test
    void givenEmptyPostRequest_whenGetAllOffersByPostRequest_thenThrowJovinnException() {
        // given - precondition or setup
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        JovinnException thrown = assertThrows(JovinnException.class,()->{
            offerRequestService.getAllOffersByPostRequest(newPostRequest.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("Không tim thấy post");
    }

    @DisplayName("Junit test for getAllOffersByPostRequest method which throw UnauthorizedException")
    @Test
    void givenEmptyPostRequest_whenGetAllOffersByPostRequest_thenThrowUnauthorizedException() {
        // given - precondition or setup
        User newUserBuyer = User.builder()
                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
                .firstName("Minh")
                .lastName("Duc")
                .username("Minhduc123")
                .email("Ducminh@gmail.com")
                .phoneNumber("0945333444")
                .gender(Gender.MALE)
                .birthDate(new Date(2000-11-11))
                .city("Quang Ninh")
                .country("Viet Nam")
                .avatar("avatarStar")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.BUYER))
                .isEnabled(true)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        // when -  action or the behaviour that we are going test
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class,()->{
            offerRequestService.getAllOffersByPostRequest(newPostRequest.getId(),UserPrincipal.create(newUserBuyer));
        });
        // then - verify the output
        assertThat(thrown.getApiResponse()).isEqualTo(new ApiResponse(Boolean.FALSE, "You don't have permission"));
    }
}