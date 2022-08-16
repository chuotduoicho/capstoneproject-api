package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.ContractRequest;
import com.jovinn.capstoneproject.dto.client.response.ContractResponse;
import com.jovinn.capstoneproject.enumerable.BoxServiceStatus;
import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.BuyerRepository;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.repository.PackageRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.util.DateDelivery;
import com.jovinn.capstoneproject.util.EmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.InstanceOfAssertFactories.DATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private DateDelivery dateDelivery;
    @Mock
    private EmailSender emailSender;
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @Mock
    private PackageRepository packageRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private WalletRepository walletRepository;
    @InjectMocks
    private ContractServiceImpl contractService;
    private User newUserBuyer;
    private User newUserSeller;
    private Buyer newBuyer;
    private Seller newSeller;
    private Package newPackage;
    private Box newBox;
    private SubCategory newSubCategory;
    private Gallery newGallery;
    private Category newCategory;
    private Wallet newWalletSeller;
    private Wallet newWalletBuyer;
    private Contract newContract;
    private User newUserBuyer1;
    private Buyer newBuyer1;
    @BeforeEach
    void setUp() {
        newUserBuyer = User.builder()
                .id(UUID.fromString("95f5ed4e-4594-46d9-94b6-8924076283a2"))
                .firstName("Tran")
                .lastName("Son")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.BUYER))
                .isEnabled(true)
                .city("Thai Nguyen")
                .buyer(newBuyer)
                .build();
        newUserSeller = User.builder()
                .id(UUID.fromString("624b69ba-1f89-483a-975f-d8b0e7c9ab8c"))
                .firstName("Nguyen")
                .lastName("Phuc")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.SELLER))
                .email("NguyenPhuc@gmail.com")
                .build();
        newSeller = Seller.builder()
                .id(UUID.fromString("9cf5d172-1a2c-4302-8689-90893f19561c"))
                .brandName("Coder Brand")
                .descriptionBio("Java backend coder")
                .sellerNumber("124524")
                .rankSeller(RankSeller.BEGINNER)
                .user(newUserSeller)
                .build();
        newBuyer = Buyer.builder()
                .id(UUID.fromString("c454700b-17f4-4249-8d97-3706cc694897"))
                .successContract(1)
                .buyerNumber("111111")
                .user(newUserBuyer)
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
                .id(UUID.fromString("3d4320ec-fef4-4130-bef4-e77fe451c8cf"))
                .price(new BigDecimal(1))
                .contractCancelFee(1)
                .box(newBox)
                .deliveryTime(1)
                .shortDescription("Short Description About This Package")
                .title("Title String")
                .build();
        newWalletSeller = Wallet.builder()
                .id(UUID.fromString("6ebbfc46-6cf7-49aa-905e-cc47a21437c2"))
                .income(new BigDecimal(1000))
                .withdraw(new BigDecimal(1))
                .user(newUserSeller)
                .build();
        newWalletBuyer = Wallet.builder()
                .id(UUID.fromString("472cb0d9-e29e-4ac3-95c4-c868287af517"))
                .income(new BigDecimal(2000))
                .withdraw(new BigDecimal(2))
                .user(newUserBuyer)
                .build();
        newContract = Contract.builder()
                .id(UUID.fromString("f4774ea2-2b21-41af-a271-1122da068a4b"))
                .packageId(newPackage.getId())
                .contractCode("Contract code")
                .requirement("String Requirement")
                .quantity(1)
                .contractCancelFee(1)
                .serviceDeposit(new BigDecimal(1))
                .totalPrice(new BigDecimal(1))
                .totalDeliveryTime(1)
                .expectCompleteDate(dateDelivery.expectDate(Calendar.DAY_OF_MONTH, 1))
                .buyer(newBuyer)
                .seller(newSeller)
                .build();
    }
    @DisplayName("Junit test for createContract method")
    @Test
    void givenData_whenCreateContract_thenReturnContractResponse() {
        // given - precondition or setup
        given(packageRepository.findById(newPackage.getId())).willReturn(Optional.of(newPackage));
        given(buyerRepository.findBuyerByUserId(newUserBuyer.getId())).willReturn(Optional.of(newBuyer));
        given(sellerRepository.findById(newSeller.getId())).willReturn(Optional.of(newSeller));
        given(walletRepository.findWalletByUserId(newUserBuyer.getId())).willReturn(newWalletBuyer);
        given(contractRepository.save(newContract)).willReturn(newContract);
        emailSender = mock(EmailSender.class,RETURNS_DEEP_STUBS);
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setQuantity(1);
        contractRequest.setRequirement("String Requirment");
        contractRequest.setPackageId(newPackage.getId());
        // when -  action or the behaviour that we are going test
        ContractResponse contractResponse = contractService.createContract(contractRequest, UserPrincipal.create(newUserBuyer));
        // then - verify the output
        assertThat(contractResponse).isNotNull();
        assertThat(contractResponse.getId()).isEqualTo(newContract.getId());
    }

    @DisplayName("Junit test for createContract method which throw Api Exception 1")
    @Test
    void givenEmptyPackage_whenCreateContract_thenThrowApiException1() {
        // given - precondition or setup
        given(packageRepository.findById(newPackage.getId())).willReturn(Optional.empty());
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPackageId(newPackage.getId());
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            contractService.createContract(contractRequest, UserPrincipal.create(newUserBuyer));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("Package not found ");
    }

    @DisplayName("Junit test for createContract method which throw Api Exception 2")
    @Test
    void givenEmptyBuyer_whenCreateContract_thenThrowApiException2() {
        // given - precondition or setup
        given(packageRepository.findById(newPackage.getId())).willReturn(Optional.of(newPackage));
        given(buyerRepository.findBuyerByUserId(newUserBuyer.getId())).willReturn(Optional.empty());
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPackageId(newPackage.getId());
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            contractService.createContract(contractRequest, UserPrincipal.create(newUserBuyer));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("Buyer not found ");
    }

    @DisplayName("Junit test for createContract method which throw Api Exception 3")
    @Test
    void givenEmptySeller_whenCreateContract_thenThrowApiException3() {
        // given - precondition or setup
        given(packageRepository.findById(newPackage.getId())).willReturn(Optional.of(newPackage));
        given(buyerRepository.findBuyerByUserId(newUserBuyer.getId())).willReturn(Optional.of(newBuyer));
        given(sellerRepository.findById(newSeller.getId())).willReturn(Optional.empty());
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setPackageId(newPackage.getId());
        // when -  action or the behaviour that we are going test
        ApiException thrown = assertThrows(ApiException.class,()->{
            contractService.createContract(contractRequest, UserPrincipal.create(newUserBuyer));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("Seller not found ");
    }
    @DisplayName("Junit test for createContract method which throw JovinnException")
    @Test
    void givenData_whenCreateContract_thenThrowJovinnException() {
        // given - precondition or setup
        newUserBuyer1 = User.builder()
                .id(UUID.fromString("eeebf088-6552-441e-b1fe-c689a8f70245"))
                .firstName("Tran")
                .lastName("Son")
                .activityType(activityTypeRepository.findByActivityType(UserActivityType.BUYER))
                .isEnabled(true)
                .city("Thai Nguyen")
                .buyer(newBuyer1)
                .build();
        newBuyer1 = Buyer.builder()
                .id(UUID.fromString("2629dbf9-8798-426c-9892-6c18eed540f1"))
                .successContract(1)
                .buyerNumber("111111")
                .user(newUserBuyer1)
                .build();
        Wallet newWalletBuyer1 = Wallet.builder()
                .id(UUID.fromString("87ae9187-f5db-43a1-bafa-fb218befce3a"))
                .income(new BigDecimal(2000))
                .withdraw(new BigDecimal(2))
                .user(newUserBuyer)
                .build();
        Box newBox1 = Box.builder()
                .id(UUID.fromString("f0b2ec62-89f0-4a3e-951e-aa889121bbe1"))
                .title("Create Web Site")
                .description("Create Web Site with java backend skill")
                .impression(0)
                .status(BoxServiceStatus.ACTIVE)
                .seller(newSeller)
                .subCategory(newSubCategory)
                .gallery(newGallery)
                .build();
        Package newPackage1 = Package.builder()
                .id(UUID.fromString("0f73c89d-f7e1-4cf0-ae75-83a864022ade"))
                .price(new BigDecimal(1000))
                .contractCancelFee(1)
                .box(newBox1)
                .deliveryTime(1)
                .shortDescription("Short Description About This Package")
                .title("Title String")
                .build();
        given(packageRepository.findById(newPackage1.getId())).willReturn(Optional.of(newPackage1));
        given(buyerRepository.findBuyerByUserId(newUserBuyer1.getId())).willReturn(Optional.of(newBuyer1));
        given(sellerRepository.findById(newSeller.getId())).willReturn(Optional.of(newSeller));
        given(walletRepository.findWalletByUserId(newUserBuyer1.getId())).willReturn(newWalletBuyer1);
        ContractRequest contractRequest = new ContractRequest();
        contractRequest.setQuantity(10);
        contractRequest.setRequirement("String Requirment");
        contractRequest.setPackageId(newPackage1.getId());
        // when -  action or the behaviour that we are going test
        JovinnException thrown = assertThrows(JovinnException.class,()->{
            contractService.createContract(contractRequest, UserPrincipal.create(newUserBuyer1));
        });
        // then - verify the output
        assertThat(thrown.getMessage()).isEqualTo("Trong tài khoản hiện không đủ tiền để thanh toán");
    }

    @Test
    void updateStatusAcceptFromSeller() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void updateStatusRejectFromSeller() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void updateStatusCancelFromBuyer() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void updateStatusAcceptDeliveryFromBuyer() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void acceptDeliveryForMilestone() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void createContractFromSellerOffer() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void createContractFromSellerApply() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void flagNotAcceptDelivery() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void getContractById() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void getContractByStatus() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void getOrders() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void getContracts() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void autoCheckCompleteContract() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void getAvatarBoth() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void getTotalRevenue() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void countTotalContractByCatId() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }

    @Test
    void getContractsByCategoryId() {
        // given - precondition or setup
        // when -  action or the behaviour that we are going test
        // then - verify the output
    }
}