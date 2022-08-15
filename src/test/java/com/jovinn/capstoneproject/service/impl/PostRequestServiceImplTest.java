package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.UserProfile;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.request.PostRequestRequest;
import com.jovinn.capstoneproject.dto.client.request.TargetSellerRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerApplyPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.ListSellerTargetPostRequestResponse;
import com.jovinn.capstoneproject.dto.client.response.PostRequestResponse;
import com.jovinn.capstoneproject.enumerable.*;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.repository.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PostRequestServiceImplTest {
    @Mock
    private PostRequestRepository postRequestRepository;
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SubCategoryRepository subCategoryRepository;
    @Mock
    private BoxRepository boxRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private MilestoneContractServiceImpl milestoneContractService;
    @InjectMocks
    private PostRequestServiceImpl postRequestService;
    private PostRequest newPostRequest;
    private Buyer newBuyer;
    private User newUser;
    private MilestoneContract newMilestoneContract;
    private Category newCategory;
    private SubCategory newSubCategory;
    private Skill newSkill;
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
        newBuyer = Buyer.builder()
                .id(UUID.fromString("c454700b-17f4-4249-8d97-3706cc694897"))
                .successContract(1)
                .buyerNumber("111111")
                .user(newUser)
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
    @DisplayName("Junit test for addPostRequest method")
    @Test
    void addPostRequest() {
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("c06f2b1d-9391-4118-872d-76b9146456cf"))
                .firstName("Minh")
                .lastName("Duc")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();
        Notification newNotification = Notification.builder()
                .id(UUID.fromString("6758d534-bdde-4e5b-ba4d-53639dbbec6d"))
                .link("link1")
                .shortContent("short content 1")
                .unread(true)
                .user(newUser1)
                .build();
        PostRequest newPostRequest1 = PostRequest.builder()
                .recruitLevel("recruitLevel2")
                .jobTitle("jobTitle2")
                .shortRequirement("shortRequirement2")
                .attachFile("attachFile2")
                .contractCancelFee(2)
                .totalDeliveryTime(2)
                .category(newCategory)
                .subCategory(newSubCategory)
                .skills(List.of(newSkill))
                .milestoneContracts(List.of(newMilestoneContract))
                .build();
        given(userRepository.findUserById(newUser.getId())).willReturn(newUser);
        given(skillRepository.findAllByNameIn(List.of(newSkill.getName()))).willReturn(List.of(newSkill));
        given(categoryRepository.findCategoryById(newCategory.getId())).willReturn(newCategory);
        given(subCategoryRepository.findSubCategoryById(newSubCategory.getId())).willReturn(newSubCategory);
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.of(newBuyer));
        given(notificationRepository.save(newNotification)).willReturn(newNotification);
        given(postRequestRepository.save(newPostRequest1)).willReturn(newPostRequest1);
        willDoNothing().given(milestoneContractService).addMilestoneContract(newMilestoneContract);
        PostRequestRequest postRequestRequest = new PostRequestRequest();
        postRequestRequest.setRecruitLevel(newPostRequest1.getRecruitLevel());
        postRequestRequest.setJobTitle(newPostRequest1.getJobTitle());
        postRequestRequest.setShortRequirement(newPostRequest1.getShortRequirement());
        postRequestRequest.setAttachFile(newPostRequest1.getAttachFile());
        postRequestRequest.setContractCancelFee(newPostRequest1.getContractCancelFee());
        postRequestRequest.setCategoryId(newPostRequest1.getCategory().getId());
        postRequestRequest.setSubCategoryId(newPostRequest1.getSubCategory().getId());
        postRequestRequest.setSkillsName(List.of(newSkill.getName()));
        postRequestRequest.setMilestoneContracts(newPostRequest1.getMilestoneContracts());
        postRequestRequest.setInvitedUsers(List.of(newUser1));
        // when -  action or the behaviour that we are going test
        ApiResponse apiResponse = postRequestService.addPostRequest(postRequestRequest, UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.TRUE, "Khởi tạo yêu cầu thành công"));
    }
    @DisplayName("Junit test for addPostRequest method throw api exception")
    @Test
    void addPostRequest_thenThrowApiException() {
        // given - precondition or setup
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.empty());
        PostRequestRequest postRequestRequest = new PostRequestRequest();
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            postRequestService.addPostRequest(postRequestRequest,UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(postRequestRepository,never()).save(any(PostRequest.class));
    }
    @DisplayName("Junit test for addPostRequest method throw UnauthorizedException exception")
    @Test
    void givenBuyerObject_whenAddPostRequest_thenThrowUnauthorizedExceptionException() {
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("c06f2b1d-9391-4118-872d-76b9146456cf"))
                .firstName("Minh")
                .lastName("Duc")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.BUYER))
                .isEnabled(false)
                .build();
        Buyer newBuyer1 = Buyer.builder()
                .id(UUID.fromString("c454700b-17f4-4249-8d97-3706cc694892"))
                .successContract(2)
                .buyerNumber("111112")
                .user(newUser1)
                .build();
        given(buyerRepository.findBuyerByUserId(newUser1.getId())).willReturn(Optional.of(newBuyer1));
        PostRequestRequest postRequestRequest = new PostRequestRequest();
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            postRequestService.addPostRequest(postRequestRequest,UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(postRequestRepository,never()).save(any(PostRequest.class));
    }
    @DisplayName("Junit test for updatePostRequest method")
    @Test
    void updatePostRequest() {
        // given - precondition or setup
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.of(newBuyer));
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(postRequestRepository.save(newPostRequest)).willReturn(newPostRequest);
        willDoNothing().given(milestoneContractService).addMilestoneContract(newMilestoneContract);
        PostRequestRequest postRequestRequest = new PostRequestRequest();
        postRequestRequest.setRecruitLevel(newPostRequest.getRecruitLevel());
        postRequestRequest.setJobTitle("jobTitle2");
        postRequestRequest.setShortRequirement("shortRequirement2");
        postRequestRequest.setAttachFile(newPostRequest.getAttachFile());
        postRequestRequest.setContractCancelFee(newPostRequest.getContractCancelFee());
        postRequestRequest.setCategoryId(newPostRequest.getCategory().getId());
        postRequestRequest.setSubCategoryId(newPostRequest.getSubCategory().getId());
        postRequestRequest.setSkillsName(List.of(newSkill.getName()));
        postRequestRequest.setMilestoneContracts(newPostRequest.getMilestoneContracts());

        // when -  action or the behaviour that we are going test
        ApiResponse apiResponse = postRequestService.updatePostRequest(postRequestRequest,newPostRequest.getId(),UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.TRUE, "Cập nhật yêu cầu thành công"));
        assertThat(newPostRequest.getJobTitle()).isEqualTo("jobTitle2");
        assertThat(newPostRequest.getShortRequirement()).isEqualTo("shortRequirement2");
    }
    @DisplayName("Junit test for updatePostRequest method which throw Api exception 1")
    @Test
    void givenEmptyBuyer_whenUpdatePostRequest_thenThrowApiException() {
        // given - precondition or setup
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.empty());
        PostRequestRequest postRequestRequest = new PostRequestRequest();
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            postRequestService.updatePostRequest(postRequestRequest,newPostRequest.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(postRequestRepository,never()).save(any(PostRequest.class));
    }
    @DisplayName("Junit test for updatePostRequest method which throw Api exception 2")
    @Test
    void givenEmptyPostRequest_whenUpdatePostRequest_thenThrowApiException() {
        // given - precondition or setup
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.of(newBuyer));
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.empty());
        PostRequestRequest postRequestRequest = new PostRequestRequest();
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            postRequestService.updatePostRequest(postRequestRequest,newPostRequest.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(postRequestRepository,never()).save(any(PostRequest.class));
    }
    @DisplayName("Junit test for updatePostRequest method which throw UnauthorizedException exception ")
    @Test
    void givenEmptyPostRequestRequest_whenUpdatePostRequest_thenThrowUnauthorizedExceptionException() {
        // given - precondition or setup
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.of(newBuyer));
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        PostRequestRequest postRequestRequest = null;
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            postRequestService.updatePostRequest(postRequestRequest,newPostRequest.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(postRequestRepository,never()).save(any(PostRequest.class));
    }
    @DisplayName("Junit test for deletePostRequest method")
    @Test
    void givenNothing_whenDeletePostRequest_thenReturnTrue() {
        // given - precondition or setup
        willDoNothing().given(postRequestRepository).deleteById(newPostRequest.getId());
        // when -  action or the behaviour that we are going test
        Boolean aBoolean = postRequestService.deletePostRequest(newPostRequest.getId());
        // then - verify the output
        assertThat(aBoolean).isEqualTo(true);
        verify(postRequestRepository,times(1)).deleteById(newPostRequest.getId());
    }
    @DisplayName("Junit test for getPostRequestByBuyerCreated method")
    @Test
    void givenData_whenGetPostRequestByBuyerCreated_thenReturnListPostRequest() {
        // given - precondition or setup
        PostRequest newPostRequest1 = PostRequest.builder()
                .id(UUID.fromString("cddaa542-acb9-4dd3-89d9-aaa1282203e2"))
                .recruitLevel("recruitLevel2")
                .jobTitle("jobTitle2")
                .shortRequirement("shortRequirement2")
                .attachFile("attachFile2")
                .contractCancelFee(2)
                .totalDeliveryTime(2)
                .category(newCategory)
                .subCategory(newSubCategory)
                .skills(List.of(newSkill))
                .milestoneContracts(List.of(newMilestoneContract))
                .user(newUser)
                .build();
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
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.of(newBuyer));
        given(postRequestRepository.findAllByUser_Id(newUser.getId())).willReturn(List.of(newPostRequest1));
        given(userService.getListUserInvitedByPostRequestId(newPostRequest1.getId())).willReturn(List.of(new UserProfile(
                newUserSeller.getId(),newUserSeller.getFirstName(),newUserSeller.getLastName(),newUserSeller.getUsername(),
                newUserSeller.getEmail(),newUserSeller.getPhoneNumber(),newUserSeller.getGender(),newUserSeller.getBirthDate(),
                newUserSeller.getCity(),newUserSeller.getCountry(),newUserSeller.getAvatar()
        )));
        // when -  action or the behaviour that we are going test
        List<PostRequestResponse> postRequestResponses = postRequestService.getPostRequestByBuyerCreated(UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(postRequestResponses).isNotNull();
        assertThat(postRequestResponses.size()).isEqualTo(1);
    }
    @DisplayName("Junit test for getPostRequestByBuyerCreated method which throw Api exception")
    @Test
    void givenEmptyBuyer_whenGetPostRequestByBuyerCreated_thenThrowApiException() {
        // given - precondition or setup
        given(buyerRepository.findBuyerByUserId(newUser.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            postRequestService.getPostRequestByBuyerCreated(UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(postRequestRepository,never()).findAllByUser_Id(any(UUID.class));
    }
    @DisplayName("Junit test for getPostRequestByBuyerCreated method which throw UnauthorizedException exception")
    @Test
    void givenDisableUser_whenGetPostRequestByBuyerCreated_thenThrowUnauthorizedExceptionException() {
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("ed77978b-bc54-4823-a08b-0cfddf11f214"))
                .firstName("Hong")
                .lastName("Phuc")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.BUYER))
                .isEnabled(false)
                .build();
        Buyer newBuyer1 = Buyer.builder()
                .id(UUID.fromString("5b6c9bcf-25ca-490a-bdc9-d6571c8a3f0a"))
                .successContract(2)
                .buyerNumber("111112")
                .user(newUser1)
                .build();
        given(buyerRepository.findBuyerByUserId(newUser1.getId())).willReturn(Optional.of(newBuyer1));
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            postRequestService.getPostRequestByBuyerCreated(UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(postRequestRepository,never()).findAllByUser_Id(any(UUID.class));
    }
    @DisplayName("Junit test for getTargetSeller method")
    @Test
    void givenData_whenGetTargetSeller_thenReturnListSellerTargetPostRequestResponse() {
        // given - precondition or setup
        PostRequest newPostRequest1 = PostRequest.builder()
                .id(UUID.fromString("cddaa542-acb9-4dd3-89d9-aaa1282203e2"))
                .recruitLevel("recruitLevel2")
                .jobTitle("jobTitle2")
                .shortRequirement("shortRequirement2")
                .attachFile("attachFile2")
                .contractCancelFee(2)
                .totalDeliveryTime(2)
                .category(newCategory)
                .subCategory(newSubCategory)
                .skills(List.of(newSkill))
                .milestoneContracts(List.of(newMilestoneContract))
                .user(newUser)
                .build();
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
                .skills(List.of(newSkill))
                .build();
        Box newBox = Box.builder()
                .id(UUID.fromString("2dc8afe1-a67f-45e9-a125-f605cf08b286"))
                .title("Create Web Site")
                .description("Create Web Site with java backend skill")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
                .build();
        TargetSellerRequest targetSellerRequest = new TargetSellerRequest();
        targetSellerRequest.setSubCategoryId(newSubCategory.getId());
        targetSellerRequest.setRankSeller(newSeller.getRankSeller());
        targetSellerRequest.setSkillName(Set.of(newSkill.getName()));
        given(sellerRepository.getTenSellerBySubCategoryId(targetSellerRequest.getSubCategoryId(),
                targetSellerRequest.getRankSeller(),targetSellerRequest.getSkillName(),
                PageRequest.of(0,10))).willReturn(List.of(newSeller.getId().toString()));
        given(sellerRepository.findById(UUID.fromString(newSeller.getId().toString()))).
                willReturn(Optional.of(newSeller));
        // when -  action or the behaviour that we are going test
        List<ListSellerTargetPostRequestResponse> listSellerTargetPostRequestResponses =
                postRequestService.getTargetSeller(targetSellerRequest);
        // then - verify the output
        assertThat(listSellerTargetPostRequestResponses).isNotNull();
        assertThat(listSellerTargetPostRequestResponses.size()).isEqualTo(1);
    }
    @DisplayName("Junit test for getTargetSeller method which throw exception")
    @Test
    void givenData_whenGetTargetSeller_thenThrowException() {
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
                .skills(List.of(newSkill))
                .build();
        TargetSellerRequest targetSellerRequest = new TargetSellerRequest();
        targetSellerRequest.setSubCategoryId(newSubCategory.getId());
        targetSellerRequest.setRankSeller(newSeller.getRankSeller());
        targetSellerRequest.setSkillName(Set.of(newSkill.getName()));
        given(sellerRepository.getTenSellerBySubCategoryId(targetSellerRequest.getSubCategoryId(),
                targetSellerRequest.getRankSeller(),targetSellerRequest.getSkillName(),
                PageRequest.of(0,10))).willReturn(List.of(newSeller.getId().toString()));
        given(sellerRepository.findById(UUID.fromString(newSeller.getId().toString()))).
                willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(JovinnException.class,()->{
            postRequestService.getTargetSeller(targetSellerRequest);
        });
        // then - verify the output
        verify(sellerRepository,times(1)).findById(any(UUID.class));
    }
    @DisplayName("Junit test for getPostRequestByCategoryId method")
    @Test
    void givenCategoryId_whenGetPostRequestByCategoryId_thenReturnPostRequestResponse() {
        // given - precondition or setup
        given(postRequestRepository.findAllByCategory_Id(newCategory.getId())).willReturn(List.of(newPostRequest));
        // when -  action or the behaviour that we are going test
        List<PostRequestResponse> postRequestResponses = postRequestService.getPostRequestByCategoryId(newCategory.getId());
        // then - verify the output
        assertThat(postRequestResponses).isNotNull();
        assertThat(postRequestResponses.size()).isEqualTo(1);
    }
    @DisplayName("Junit test for getPostRequestDetails method")
    @Test
    void givenPostRequestId_whenGetPostRequestDetails_thenReturnPostRequestResponse() {
        // given - precondition or setup
        given(postRequestRepository.findPostRequestById(newPostRequest.getId())).willReturn(newPostRequest);
        // when -  action or the behaviour that we are going test
        PostRequestResponse postRequestResponse = postRequestService.getPostRequestDetails(newPostRequest.getId());
        // then - verify the output
        assertThat(postRequestResponse).isNotNull();
    }
    @DisplayName("Junit test for getPostRequestDetails method (negative scenario)")
    @Test
    void givenPostRequestId_whenGetPostRequestDetails_thenReturnEmptyPostRequestResponse() {
        // given - precondition or setup
        given(postRequestRepository.findPostRequestById(newPostRequest.getId())).willReturn(null);
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(NullPointerException.class,()->{
            postRequestService.getPostRequestDetails(newPostRequest.getId());
        });
        // then - verify the output
        verify(postRequestRepository,times(1)).findPostRequestById(any(UUID.class));
    }
//    @DisplayName("Junit test for sellerApplyRequest method")
//    @Test
//    void givenData_whenSellerApplyRequest_thenReturnApiResponseTrue() {
//        // given - precondition or setup
//        User newUserSeller = User.builder()
//                .id(UUID.fromString("b9a1a7fe-204f-4acc-8412-0f71990d77e8"))
//                .firstName("Minh")
//                .lastName("Duc")
//                .username("Minhduc123")
//                .email("Ducminh@gmail.com")
//                .phoneNumber("0945333444")
//                .gender(Gender.MALE)
//                .birthDate(new Date(2000-11-11))
//                .city("Quang Ninh")
//                .country("Viet Nam")
//                .avatar("avatarStar")
//                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
//                .isEnabled(true)
//                .build();
//        Seller newSeller = Seller.builder()
//                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
//                .brandName("Coder Brand")
//                .descriptionBio("Java backend coder")
//                .sellerNumber("124524")
//                .rankSeller(RankSeller.BEGINNER)
//                .user(newUserSeller)
//                .skills(List.of(newSkill))
//                .totalOrderFinish(1)
//                .ratingPoint(1)
//                .verifySeller(true)
//                .build();
//        Seller newSeller1 = Seller.builder()
//                .id(UUID.fromString("3f2b9a60-6d72-4851-a4f3-66e364945cec"))
//                .brandName("Coder Brand 1")
//                .descriptionBio("Java backend coder 1")
//                .sellerNumber("124525")
//                .rankSeller(RankSeller.BEGINNER)
//                .user(mock(User.class,RETURNS_DEEP_STUBS))
//                .skills(List.of(newSkill))
//                .build();
//        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
//        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
//        given(sellerRepository.findAllByPostRequests_Id(newPostRequest.getId())).willReturn(List.of(newSeller1));
//        // when -  action or the behaviour that we are going test
//        ApiResponse apiResponse = postRequestService.sellerApplyRequest(newPostRequest.getId(),UserPrincipal.create(newUserSeller));
//        // then - verify the output
//        assertThat(apiResponse).isNotNull();
//        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.TRUE,"Apply Post Request thành công"));
//    }

    @DisplayName("Junit test for sellerApplyRequest method return ApiResponse False")
    @Test
    void givenData_whenSellerApplyRequest_thenReturnApiResponseFalse() {
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
                .skills(List.of(newSkill))
                .totalOrderFinish(1)
                .ratingPoint(1)
                .verifySeller(true)
                .build();
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        given(sellerRepository.findAllByPostRequests_Id(newPostRequest.getId())).willReturn(List.of(newSeller));
        // when -  action or the behaviour that we are going test
        ApiResponse apiResponse = postRequestService.sellerApplyRequest(newPostRequest.getId(),UserPrincipal.create(newUserSeller));
        // then - verify the output
        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.FALSE, "Bạn đã apply bài Post Request này"));
    }

    @DisplayName("Junit test for sellerApplyRequest method which throw ApiException 1")
    @Test
    void givenEmptySeller_whenSellerApplyRequest_thenThrowApiException1() {
        // given - precondition or setup
        given(sellerRepository.findSellerByUserId(newUser.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            postRequestService.sellerApplyRequest(newPostRequest.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(sellerRepository,times(1)).findSellerByUserId(any(UUID.class));
    }

    @DisplayName("Junit test for sellerApplyRequest method which throw ApiException 2")
    @Test
    void givenEmptyPostRequest_whenSellerApplyRequest_thenThrowApiException2() {
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
                .skills(List.of(newSkill))
                .totalOrderFinish(1)
                .ratingPoint(1)
                .verifySeller(true)
                .build();
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            postRequestService.sellerApplyRequest(newPostRequest.getId(),UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        verify(postRequestRepository,times(1)).findById(any(UUID.class));
    }

    @DisplayName("Junit test for sellerApplyRequest method which throw UnauthorizedException")
    @Test
    void givenEmptySeller_whenSellerApplyRequest_thenThrowUnauthorizedException() {
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
                .isEnabled(false)
                .build();
        Seller newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .skills(List.of(newSkill))
                .totalOrderFinish(1)
                .ratingPoint(1)
                .verifySeller(true)
                .build();
        given(sellerRepository.findSellerByUserId(newUserSeller.getId())).willReturn(Optional.of(newSeller));
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            ApiResponse apiResponse = postRequestService.sellerApplyRequest(newPostRequest.getId(),UserPrincipal.create(newUserSeller));
            // then - verify the output
            assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.FALSE, "You don't have permission"));
        });

    }
    @DisplayName("Junit test for getListSellerApply method")
    @Test
    void givenPostRequestId_whenGetListSellerApply_thenReturnListSellerApplyPostRequestResponse() {
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
                .isEnabled(false)
                .build();
        Seller newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .skills(List.of(newSkill))
                .totalOrderFinish(1)
                .ratingPoint(1)
                .verifySeller(true)
                .build();
        PostRequest newPostRequest1 = PostRequest.builder()
                .id(UUID.fromString("1a7e5e61-7613-4039-bf99-d50ef64ac930"))
                .status(PostRequestStatus.OPEN)
                .recruitLevel("recruitLevel2")
                .jobTitle("jobTitle2")
                .shortRequirement("shortRequirement2")
                .attachFile("attachFile2")
                .contractCancelFee(1)
                .totalDeliveryTime(1)
                .budget(new BigDecimal(1))
                .category(newCategory)
                .subCategory(newSubCategory)
                .skills(List.of(newSkill))
                .user(newUser)
                .sellersApplyRequest(List.of(newSeller))
                .milestoneContracts(List.of(newMilestoneContract))
                .build();

        given(postRequestRepository.findById(newPostRequest1.getId())).willReturn(Optional.of(newPostRequest1));
        // when -  action or the behaviour that we are going test
        ListSellerApplyPostRequestResponse response = postRequestService.getListSellerApply(newPostRequest1.getId(),UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(response).isNotNull();
        assertThat(response.getPostRequestId()).isEqualTo(newPostRequest1.getId());
    }
    @DisplayName("Junit test for getListSellerApply method which throw ApiException")
    @Test
    void givenEmptyPostRequestId_whenGetListSellerApply_thenThrowApiException() {
        // given - precondition or setup
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            postRequestService.getListSellerApply(newPostRequest.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(postRequestRepository,times(1)).findById(any(UUID.class));
    }
    @DisplayName("Junit test for getListSellerApply method which throw UnauthorizedException")
    @Test
    void givenPostRequest_whenGetListSellerApply_thenThrowUnauthorizedException() {
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
                .isEnabled(false)
                .build();
        Seller newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .skills(List.of(newSkill))
                .totalOrderFinish(1)
                .ratingPoint(1)
                .verifySeller(true)
                .build();
        given(postRequestRepository.findById(newPostRequest.getId())).willReturn(Optional.of(newPostRequest));
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            postRequestService.getListSellerApply(newPostRequest.getId(),UserPrincipal.create(newUserSeller));
        });
        // then - verify the output
        verify(postRequestRepository,times(1)).findById(any(UUID.class));
    }
    @DisplayName("Junit test for getAllPostRequest method")
    @Test
    void givenListPostRequest_whenGetAllPostRequest_thenReturnListPostRequest() {
        // given - precondition or setup
        given(postRequestRepository.findAll()).willReturn(List.of(newPostRequest));
        // when -  action or the behaviour that we are going test
        List<PostRequestResponse> postRequestResponses = postRequestService.getAllPostRequest();
        // then - verify the output
        assertThat(postRequestResponses).isNotNull();
        assertThat(postRequestResponses.size()).isEqualTo(1);
    }

    @DisplayName("Junit test for getAllPostRequest method (negative scenario)")
    @Test
    void givenEmptyListPostRequest_whenGetAllPostRequest_thenReturnEmptyList() {
        // given - precondition or setup
        given(postRequestRepository.findAll()).willReturn(List.of());
        // when -  action or the behaviour that we are going test
        List<PostRequestResponse> postRequestResponses = postRequestService.getAllPostRequest();
        // then - verify the output
        assertThat(postRequestResponses).isEmpty();
        assertThat(postRequestResponses.size()).isEqualTo(0);
    }
    @DisplayName("Junit test for countTotalPostRequestByCatId method")
    @Test
    void countTotalPostRequestByCatId() {
        // given - precondition or setup
        given(postRequestRepository.countPostRequestByCategory_Id(newCategory.getId())).willReturn(1l);
        // when -  action or the behaviour that we are going test
        CountPostRequestResponse countPostRequestResponse = postRequestService.countTotalPostRequestByCatId(newCategory.getId());
        // then - verify the output
        assertThat(countPostRequestResponse.getTotalPostRequest()).isEqualTo(1);
    }
}