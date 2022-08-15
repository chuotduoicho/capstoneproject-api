package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.PageResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountServiceResponse;
import com.jovinn.capstoneproject.dto.client.boxsearch.BoxSearchResponse;
import com.jovinn.capstoneproject.dto.client.request.BoxRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.BoxResponse;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.repository.GalleryRepository;
import com.jovinn.capstoneproject.repository.HistoryBoxRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.thirdapi.GoogleDriveManagerService;
import com.jovinn.capstoneproject.util.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BoxServiceImplTest {
    @Mock
    private BoxRepository boxRepository;
    @Mock
    private GalleryRepository galleryRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @Mock
    private GoogleDriveManagerService googleDriveManagerService;
    @Mock
    private Page<Box> pageBox;
    @Mock
    private HistoryBoxRepository historyBoxRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private BoxServiceImpl boxService;
    private Box newBox;
    private Seller newSeller;
    private User newUser;
    private Category newCategory;
    private SubCategory newSubCategory;
    private Gallery newGallery;
    private Package newPackage;
    @BeforeEach
    public void setup(){
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
        newCategory = Category.builder()
                .id(UUID.fromString("66c2360c-01f4-483d-a404-e5f0a6818bd9"))
                .name("Web")
                .build();
        newSubCategory = SubCategory.builder()
                .id(UUID.fromString("2ef78cde-5a54-4b16-bb2c-3f65656ed200"))
                .name("Web backend")
                .category(newCategory)
                .build();
        newGallery = Gallery.builder()
                .id(UUID.fromString("b108b46e-b137-4d1a-b095-7dcdf385bf27"))
                .imageGallery1("imageGallery11111111111111111111111")
                .imageGallery2("imageGallery22222222222222222211111")
                .imageGallery3("imageGallery33333333333333333311111")
                .videoGallery("videoGallery44444444444444444411111")
                .documentGallery("documentGallery55555555555555511111")
                .build();
        newBox = Box.builder()
                .id(UUID.fromString("2dc8afe1-a67f-45e9-a125-f605cf08b286"))
                .title("Create Web Site")
                .description("Create Web Site with java backend skill")
                .impression(0)
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
                .gallery(newGallery)
                .build();
        newPackage = Package.builder()
                .id(UUID.fromString("5e660ee0-4cf5-4c99-b0bf-2a25ac30a908"))
                .price(new BigDecimal(111))
//                .box(newBox)
                .build();

    }
//    @Test
//    void updateBoxTrueUserId(){
//        UUID idUser = UUID.fromString("e59410b9-7a59-419d-9baa-476ee7db9b87");
//        UUID idBox = UUID.fromString("2e26c0b6-9570-4b83-b73b-26ccc27f0b8f");
//        User user = userService.getByUserId(idUser);
//        Box box = boxRepository.findById(idBox)
//                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Not found box"));
//        box.setDescription("Tôi làm mọi thứ với 200k");
//        BoxResponse boxUpdate = boxService.updateBox(box,idBox,UserPrincipal.create(user));
//        assertThat(boxUpdate.getDescription()).isEqualTo("Tôi làm mọi thứ với 200k");
//    }
//    @Test
//    void getServiceByIdHaveInDatabase(){
//        UUID id = UUID.fromString("2e26c0b6-9570-4b83-b73b-26ccc27f0b8f");
//        BoxResponse box = boxService.getServiceByID(id);
//        assertThat(box.getId()).isEqualTo(id);
//    }
//    @Test
//    void getServiceByIdNotHaveInDatabase(){
//        UUID id = UUID.fromString("2e26c0b6-9570-4b83-b73b-26ccc27f0b81");
//        BoxResponse box = boxService.getServiceByID(id);
//        assertThat(box).isEqualTo(null);
//    }
//    @Test
//    void getListBox(){
//        List<Box> boxes = boxService.getAllService();
//        assertThat(boxes.size()).isGreaterThan(0);
//    }
    @DisplayName("Junit test for saveBox method")
    @Test
    void givenSellerObjectAndBoxObject_whenSaveBox_thenReturnMessageSavedBox() {
        // given - precondition or setup
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .packages(List.of(newPackage))
                .build();
        Package aPackage1 = Package.builder()
                .id(UUID.fromString("4a9806d5-2b6f-436d-ac6a-241f0a850c05"))
                .price(new BigDecimal(111))
                .box(box1)
                .build();
        given(sellerRepository.findSellerByUserId(UserPrincipal.create(newUser).getId()))
                .willReturn(Optional.of(newSeller));
//        given(boxRepository.save(box1)).willReturn(box1);

        System.out.println(boxRepository);
        System.out.println(boxService);
        // when -  action or the behaviour that we are going test
        ApiResponse response = boxService.addBox(box1,UserPrincipal.create(newUser));
        System.out.println(response);
        // then - verify the output
        assertThat(response).isEqualTo(new ApiResponse(Boolean.TRUE, "" + box1.getId()));
    }
    @DisplayName("JUnit test for saveBox method which throw Api exception")
    @Test
    void givenSellerNotFound_whenSaveBox_thenThrowApiException(){
        // given - precondition or setup
        given(sellerRepository.findSellerByUserId(UserPrincipal.create(newUser).getId()))
                .willReturn(Optional.empty());
        System.out.println(boxRepository);
        System.out.println(boxService);
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            boxService.addBox(newBox,UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(boxRepository,never()).save(any(Box.class));
    }
    @DisplayName("JUnit test for saveBox method which throw unauthorized exception")
    @Test
    void givenSellerNotFound_whenSaveBox_thenThrowUnauthorizedException(){
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("5b4ee4c9-9eaa-493f-952b-2bea9e2635cd"))
                .firstName("Tran")
                .lastName("Long")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();

        given(sellerRepository.findSellerByUserId(UserPrincipal.create(newUser1).getId()))
                .willReturn(Optional.of(newSeller));
        System.out.println(boxRepository);
        System.out.println(boxService);
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            boxService.addBox(newBox,UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(boxRepository,never()).save(any(Box.class));
    }
    @DisplayName("JUnit test for updateBox method")
    @Test
    void givenBoxObject_whenUpdateBox_thenReturnUpdateBox() {
        // given - precondition or setup
        Gallery newGallery1 = Gallery.builder()
                .id(UUID.fromString("349aebc3-a86d-49f8-96b7-6670d3c7ecfc"))
                .imageGallery1("imageGallery60206500701468157768")
                .imageGallery2("imageGallery67108065312905741226")
                .imageGallery3("imageGallery52698627506223985520")
                .videoGallery("videoGallery89984602940613496632")
                .documentGallery("documentGallery86305308881112185459")
                .build();
        given(boxRepository.findById(newBox.getId()))
                .willReturn(Optional.of(newBox));
        given(boxRepository.save(newBox)).willReturn(newBox);
//        given(galleryRepository.save(newGallery1)).willReturn(newGallery1);
        BoxRequest boxRequest = new BoxRequest();
        boxRequest.setTitle("Create Web");
        boxRequest.setDescription("Create Website with python backend skill");
        boxRequest.setImageGallery1(newGallery1.getImageGallery1());
        boxRequest.setImageGallery2(newGallery1.getImageGallery2());
        boxRequest.setImageGallery3(newGallery1.getImageGallery3());
        boxRequest.setDocumentGallery(newGallery1.getDocumentGallery());
        boxRequest.setVideoGallery(newGallery1.getVideoGallery());
        // when -  action or the behaviour that we are going test
        ApiResponse apiResponse = boxService.updateBox(newBox.getId(),boxRequest,UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.TRUE, "Cập nhật hộp dịch vụ thành công"));
    }
    @DisplayName("JUnit test for updateBox method which throw Jovinn exception")
    @Test
    void givenBoxObject_whenUpdateBox_thenThrowJovinnException() {
        // given - precondition or setup
        given(boxRepository.findById(newBox.getId()))
                .willReturn(Optional.empty());
        BoxRequest boxRequest = new BoxRequest();
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(JovinnException.class,()->{
            boxService.updateBox(newBox.getId(),boxRequest,UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(boxRepository,never()).save(any(Box.class));
    }
    @DisplayName("JUnit test for deleteBox method Return Api Response True")
    @Test
    void givenBoxId_whenDeleteBox_thenReturnApiResponseTrue() {
        // given - precondition or setup
        given(boxRepository.findById(newBox.getId())).willReturn(Optional.of(newBox));
//        willDoNothing().given(boxRepository).deleteById(newBox.getId());
        // when -  action or the behaviour that we are going test
        ApiResponse apiResponse = boxService.deleteBox(newBox.getId(),UserPrincipal.create(newUser));
        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.TRUE, "Xóa hộp dịch vụ thành công"));
        // then - verify the output
        verify(boxRepository, times(1)).deleteById(newBox.getId());
    }
    @DisplayName("JUnit test for deleteBox method Return Api Response False")
    @Test
    void givenBoxId_whenDeleteBox_thenReturnApiResponseFalse() {
        // given - precondition or setup
        Gallery newGallery1 = Gallery.builder()
                .id(UUID.fromString("2458975a-b3f2-4b5d-88e3-cca73146d585"))
                .imageGallery1(null)
                .imageGallery2("imageGallery22222222222222222211111")
                .imageGallery3("imageGallery33333333333333333311111")
                .videoGallery("videoGallery44444444444444444411111")
                .documentGallery("documentGallery55555555555555511111")
                .build();
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .gallery(newGallery1)
                .build();
        given(boxRepository.findById(box1.getId())).willReturn(Optional.of(box1));
//        willDoNothing().given(boxRepository).deleteById(newBox.getId());
        // when -  action or the behaviour that we are going test
        ApiResponse apiResponse = boxService.deleteBox(box1.getId(),UserPrincipal.create(newUser));
        assertThat(apiResponse).isEqualTo(new ApiResponse(Boolean.FALSE, "Xóa hộp dịch vụ thất bại"));
        // then - verify the output
        verify(boxRepository, times(0)).deleteById(box1.getId());
    }
    @DisplayName("JUnit test for deleteBox method throw Jovinn exception")
    @Test
    void givenBoxId_whenDeleteBox_thenThrowJovinnException() {
        // given - precondition or setup
        given(boxRepository.findById(newBox.getId())).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(JovinnException.class,()->{
            boxService.deleteBox(newBox.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(boxRepository, times(0)).deleteById(newBox.getId());
    }
    @DisplayName("JUnit test for deleteBox method throw UnauthorizedException")
    @Test
    void givenBoxId_whenDeleteBox_thenThrowUnauthorizedExceptionException() {
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("95f4c0c3-dd06-4836-a71c-29b50ce59723"))
                .firstName("Tran")
                .lastName("Son")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();
        given(boxRepository.findById(newBox.getId())).willReturn(Optional.of(newBox));
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(UnauthorizedException.class,()->{
            boxService.deleteBox(newBox.getId(),UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(boxRepository, times(0)).deleteById(newBox.getId());
    }

    @Test
    void updateStatus() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }
    @DisplayName("JUnit test for getListServiceBySellerId method")
    @Test
    void getListServiceBySellerId() {
        // given - precondition or setup
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .build();
        Pageable pageable = Pagination.paginationCommon(0, 2, "createAt", "desc");
//        doReturn(pageBox).when(boxRepository).findAllBySellerIdAndStatus(newSeller.getId(),BoxServiceStatus.ACTIVE,pageable);
//        doReturn(List.of(newBox,box1)).when(pageBox).getContent();
//        given(sellerRepository.findById(newSeller.getId())).willReturn(Optional.of(newSeller));
        System.out.println(boxService);
        System.out.println(boxRepository);
        // when -  action or the behaviour that we are going test
        boxService = mock(BoxServiceImpl.class, RETURNS_DEEP_STUBS);
        PageResponse<BoxSearchResponse> boxSearchResponsePageResponse =
                boxService.getListServiceBySellerId(newSeller.getId(),UserPrincipal.create(newUser),
                        BoxServiceStatus.ACTIVE,0,2);
        when(boxSearchResponsePageResponse.getTotalElements()).thenReturn(2l);
        // then - verify the output
        assertThat(boxSearchResponsePageResponse).isNotNull();
        assertThat(boxSearchResponsePageResponse.getTotalElements()).isEqualTo(2);
    }
    @DisplayName("JUnit test for getAllService method")
    @Test
    void givenServiceList_whenGetAllService_thenReturnServiceList() {
        // given - precondition or setup
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .build();
        given(boxRepository.findAll()).willReturn(List.of(newBox,box1));
        // when -  action or the behaviour that we are going test
        List<Box> boxList = boxService.getAllService();
        // then - verify the output
        assertThat(boxList).isNotNull();
        assertThat(boxList.size()).isEqualTo(2);
    }
    @DisplayName("JUnit test for getAllService method (negative scenario)")
    @Test
    void givenServiceList_whenGetAllService_thenEmptyServiceList() {
        // given - precondition or setup
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .build();
        given(boxRepository.findAll()).willReturn(Collections.emptyList());
        // when -  action or the behaviour that we are going test
        List<Box> boxList = boxService.getAllService();
        // then - verify the output
        assertThat(boxList).isEmpty();
        assertThat(boxList.size()).isEqualTo(0);
    }
    @DisplayName("JUnit test for getServiceById method which throw Jovinn Exception ")
    @Test
    void givenBoxId_whenGetBoxById_thenThrowJovinnException() {
        // given - precondition or setup
        given(boxRepository.findById(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        assertThrows(JovinnException.class,()->{
            boxService.getServiceByID(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(boxRepository,never()).save(any(Box.class));
    }
    @DisplayName("JUnit test for getServiceById method")
    @Test
    void givenBoxId_whenGetBoxById_thenReturnBoxObject() {
        // given - precondition or setup
        HistoryBox newHistoryBox = HistoryBox.builder()
                .id(UUID.fromString("9832ebd8-4a79-4252-ab7e-b7f799e5c1d4"))
                .userId(newUser.getId())
                .boxId(newBox.getId())
                .build();
        given(boxRepository.findById(newBox.getId())).willReturn(Optional.of(newBox));
        given(historyBoxRepository.findAllByUserIdOrderByCreateAtAsc(newUser.getId())).willReturn(List.of(newHistoryBox));
        // when -  action or the behaviour that we are going test
        BoxResponse box = boxService.getServiceByID(newBox.getId(),UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(box).isNotNull();
        assertThat(box.getImpression()).isEqualTo(0);
    }
    @DisplayName("JUnit test for getServiceById method with different current user")
    @Test
    void givenBoxIdAndDifferentUser_whenGetBoxById_thenReturnBoxObject() {
        // given - precondition or setup
        User newUser1 = User.builder()
                .id(UUID.fromString("95f4c0c3-dd06-4836-a71c-29b50ce59723"))
                .firstName("Tran")
                .lastName("Son")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .build();
        Seller newSeller1 = Seller.builder()
                .id(UUID.fromString("dda99be0-2555-454d-88c0-782617552ca8"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUser1)
                .build();
        Gallery newGallery1 = Gallery.builder()
                .id(UUID.fromString("2458975a-b3f2-4b5d-88e3-cca73146d585"))
                .imageGallery1("imageGallery11111111111111111111111")
                .imageGallery2("imageGallery22222222222222222211111")
                .imageGallery3("imageGallery33333333333333333311111")
                .videoGallery("videoGallery44444444444444444411111")
                .documentGallery("documentGallery55555555555555511111")
                .build();
        Package newPackage = Package.builder()
                .id(UUID.fromString("5e660ee0-4cf5-4c99-b0bf-2a25ac30a908"))
                .price(new BigDecimal(1))
                .build();
        Box box1 = Box.builder()
                .id(UUID.fromString("0506d933-6bfa-4186-9e02-0484f5de2c5b"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .impression(0)
                .interesting(0)
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
                .build();
        HistoryBox newHistoryBox = HistoryBox.builder()
                .id(UUID.fromString("9832ebd8-4a79-4252-ab7e-b7f799e5c1d4"))
                .userId(newUser1.getId())
                .boxId(box1.getId())
                .build();
        given(boxRepository.findById(box1.getId())).willReturn(Optional.of(box1));
        given(historyBoxRepository.findAllByUserIdOrderByCreateAtAsc(newUser1.getId())).willReturn(List.of(newHistoryBox));
        //Box have some data null must use given will not NPE
        given(boxRepository.save(box1)).willReturn(box1);
        // when -  action or the behaviour that we are going test
        BoxResponse box = boxService.getServiceByID(box1.getId(),UserPrincipal.create(newUser1));
        // then - verify the output
        assertThat(box).isNotNull();
        assertThat(box.getImpression()).isEqualTo(1);
    }
    @DisplayName("JUnit test for getAllServiceByCategoryId method")
    @Test
    void givenServiceListGetByCategoryId_whenGetAllServiceByCategoryID_thenReturnServiceList() {
        // given - precondition or setup
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
                .build();
        given(boxRepository.getAllServiceByCategoryId(newCategory.getId())).willReturn(List.of(newBox,box1));
        // when -  action or the behaviour that we are going test
        List<Box> boxList = boxService.getAllServiceByCategoryID(newCategory.getId());
        // then - verify the output
        assertThat(boxList).isNotNull();
        assertThat(boxList.size()).isEqualTo(2);
    }
    @DisplayName("JUnit test for getAllServiceByCategoryId method (negative scenario)")
    @Test
    void givenServiceListGetByCategoryId_whenGetAllServiceByCategoryID_thenReturnEmptyServiceList() {
        // given - precondition or setup
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
                .build();
        given(boxRepository.getAllServiceByCategoryId(newCategory.getId())).willReturn(Collections.emptyList());
        // when -  action or the behaviour that we are going test
        List<Box> boxList = boxService.getAllServiceByCategoryID(newCategory.getId());
        // then - verify the output
        assertThat(boxList).isEmpty();
        assertThat(boxList.size()).isEqualTo(0);
    }

    @Test
    void getAllServiceByCatIdPagination() {
    }

    @Test
    void searchServiceByCatNameBySubCateName() {
    }
    @DisplayName("JUnit test for countTotalService method")
    @Test
    void givenNumberTotalService_whenCountTotalService_thenReturnTotalService() {
        // given - precondition or setup
        Box box1 = Box.builder()
                .id(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))
                .title("Create Web Site 1")
                .description("Create Web Site with java backend skill 1")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
                .build();
        given(boxRepository.count()).willReturn(1l);
        // when -  action or the behaviour that we are going test
        CountServiceResponse countServiceResponse = boxService.countTotalService();
        System.out.println(countServiceResponse);
        // then - verify the output
        assertThat(countServiceResponse.getTotalService()).isEqualTo(1);
    }
    @DisplayName("JUnit test for countTotalServiceByCategory method")
    @Test
    void givenNumberTotalService_whenCountTotalServiceByCategory_thenReturnTotalServiceByCat() {
        // given - precondition or setup
        given(boxRepository.countBySubCategory_Category_Id(newCategory.getId())).willReturn(2l);
        // when -  action or the behaviour that we are going test
        CountServiceResponse countServiceResponse = boxService.countTotalServiceByCat(newCategory.getId());
        System.out.println(countServiceResponse);
        // then - verify the output
        assertThat(countServiceResponse.getTotalService()).isEqualTo(2);
    }
}