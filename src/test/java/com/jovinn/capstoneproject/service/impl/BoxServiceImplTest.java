package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.adminsite.CountServiceResponse;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.BoxResponse;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.repository.BoxRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.BoxService;
import com.jovinn.capstoneproject.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class BoxServiceImplTest {
    @Mock
    private BoxRepository boxRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @InjectMocks
    private BoxServiceImpl boxService;
    private Box newBox;
    private Seller newSeller;
    private User newUser;
    private Category newCategory;
    private SubCategory newSubCategory;
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
        newBox = Box.builder()
                .id(UUID.fromString("2dc8afe1-a67f-45e9-a125-f605cf08b286"))
                .title("Create Web Site")
                .description("Create Web Site with java backend skill")
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
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
        given(sellerRepository.findSellerByUserId(UserPrincipal.create(newUser).getId()))
                .willReturn(Optional.of(newSeller));
        given(boxRepository.save(newBox)).willReturn(newBox);

        System.out.println(boxRepository);
        System.out.println(boxService);
        // when -  action or the behaviour that we are going test
        ApiResponse response = boxService.saveBox(newBox,UserPrincipal.create(newUser));
        System.out.println(response);
        // then - verify the output
        assertThat(response).isEqualTo(new ApiResponse(Boolean.TRUE, "" + newBox.getId()));
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
            boxService.saveBox(newBox,UserPrincipal.create(newUser));
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
            boxService.saveBox(newBox,UserPrincipal.create(newUser1));
        });
        // then - verify the output
        verify(boxRepository,never()).save(any(Box.class));
    }
    @DisplayName("JUnit test for updateBox method")
    @Test
    void givenBoxObject_whenUpdateBox_thenReturnUpdateBox() {
        // given - precondition or setup
        given(boxRepository.findById(newBox.getId()))
                .willReturn(Optional.of(newBox));
        given(boxRepository.save(newBox)).willReturn(newBox);
        newBox.setTitle("Create Web");
        newBox.setDescription("Create Website with python backend skill");
        // when -  action or the behaviour that we are going test
        BoxResponse updateBox = boxService.updateBox(newBox,newBox.getId(),UserPrincipal.create(newUser));
        // then - verify the output
        assertThat(updateBox.getTitle()).isEqualTo("Create Web");
        assertThat(updateBox.getDescription()).isEqualTo("Create Website with python backend skill");
    }
    @DisplayName("JUnit test for updateBox method which throw exception")
    @Test
    void givenBoxObject_whenUpdateBox_thenThrowException() {
        // given - precondition or setup
        given(boxRepository.findById(newBox.getId()))
                .willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        Assertions.assertThrows(ApiException.class,()->{
            BoxResponse response = boxService.updateBox(newBox,newBox.getId(),UserPrincipal.create(newUser));
        });
        // then - verify the output
        verify(boxRepository,never()).save(any(Box.class));
    }
    @DisplayName("JUnit test for deleteBox method")
    @Test
    void givenBoxId_whenDeleteBox_thenReturnTrue() {
        // given - precondition or setup
        UUID boxID = UUID.fromString("2dc8afe1-a67f-45e9-a125-f605cf08b286");
        willDoNothing().given(boxRepository).deleteById(boxID);
        // when -  action or the behaviour that we are going test
        boolean deleteBox = boxService.deleteBox(boxID);
        assertThat(deleteBox).isEqualTo(true);
        // then - verify the output
        verify(boxRepository, times(1)).deleteById(boxID);
    }
//    @DisplayName("JUnit test for deleteBox method (negative scenario)")
//    @Test
//    void givenBoxId_whenDeleteBox_thenReturnFalse() {
//        // given - precondition or setup
//        UUID boxID = UUID.fromString("2dc8afe1-a67f-45e9-a125-f605cf08b286");
//        given(boxRepository.findById(UUID.randomUUID())).willReturn(Optional.empty());
//        // when -  action or the behaviour that we are going test
//        boxService.deleteBox(boxID);
//        // then - verify the output
//        verify(boxRepository, times(1)).deleteById(boxID);
//    }

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
        given(boxRepository.findAllBySellerId(newSeller.getId())).willReturn(List.of(newBox,box1));

        // when -  action or the behaviour that we are going test
        List<Box> boxList = boxService.getListServiceBySellerId(newSeller.getId());
        // then - verify the output
        assertThat(boxList).isNotNull();
        assertThat(boxList.size()).isEqualTo(2);
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
    @DisplayName("JUnit test for getServiceById method which throw exception")
    @Test
    void givenBoxId_whenGetBoxById_thenThrowException() {
        // given - precondition or setup
        given(boxRepository.findById(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"))).willReturn(Optional.empty());
        // when -  action or the behaviour that we are going test
        assertThrows(JovinnException.class,()->{
            boxService.getServiceByID(UUID.fromString("201ea886-b0dc-4b1c-ac66-7ac7e3c2051d"));
        });
        // then - verify the output
        verify(boxRepository).findById(any(UUID.class));
    }
    @DisplayName("JUnit test for getServiceById method")
    @Test
    void givenBoxId_whenGetBoxById_thenReturnBoxObject() {
        // given - precondition or setup
        given(boxRepository.findById(UUID.fromString("2dc8afe1-a67f-45e9-a125-f605cf08b286"))).willReturn(Optional.of(newBox));
        // when -  action or the behaviour that we are going test
        BoxResponse box = boxService.getServiceByID(newBox.getId());
        // then - verify the output
        assertThat(box).isNotNull();
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
        // then - verify the output
        assertThat(countServiceResponse.getTotalService()).isEqualTo(2);
    }
}