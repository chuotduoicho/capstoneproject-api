package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.ContractRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.ContractResponse;
import com.jovinn.capstoneproject.enumerable.*;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.model.Package;
import com.jovinn.capstoneproject.repository.*;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.ContractService;
import com.jovinn.capstoneproject.util.DateDelivery;
import com.jovinn.capstoneproject.util.EmailSender;
import com.jovinn.capstoneproject.util.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.jovinn.capstoneproject.util.GenerateRandom.getRandomContractNumber;

@Service
public class ContractServiceImpl implements ContractService {
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private OfferRequestRepository offerRequestRepository;
    @Autowired
    private PostRequestRepository postRequestRepository;
    @Autowired
    private MilestoneContractRepository milestoneContractRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private DateDelivery dateDelivery;
    @Override
    public ContractResponse createContract(ContractRequest request, UserPrincipal currentUser) throws RuntimeException {
        Package pack = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Package not found "));
        UUID sellerId = pack.getBox().getSeller().getId();
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found "));
        //PackageOptional packageOptional = packageOptionalRepository.findPackageOptionalByPackId(pack.getId())
        //.orElseThrow(() -> new ResourceNotFoundException("PackageOptional", "PackageOptional not found ", pack.getId()));
        Wallet walletBuyer = walletRepository.findWalletByUserId(currentUser.getId());
        String contractCode = getRandomContractNumber();

        Integer contractCancelFee;
        if (pack.getContractCancelFee() == null) {
            contractCancelFee = 0;
        } else {
            contractCancelFee = pack.getContractCancelFee();
        }
        //double extraPrice = packageOptional.getOptionPrice();
        Integer countTotalDeliveryTime = pack.getDeliveryTime() * request.getQuantity();
        Date expectCompleteDate = dateDelivery.expectDate(Calendar.DAY_OF_MONTH, countTotalDeliveryTime);
        BigDecimal totalPrice = scale2(pack.getPrice().multiply(new BigDecimal(request.getQuantity())));
        BigDecimal serviceDeposit = totalPrice.multiply(new BigDecimal(contractCancelFee)).divide(ONE_HUNDRED, RoundingMode.FLOOR);

        if (buyer.getUser().getId().equals(currentUser.getId()) &&
                buyer.getUser().getIsEnabled().equals(Boolean.TRUE)) {
            if (walletBuyer.getWithdraw().compareTo(totalPrice.add(serviceDeposit)) >= 0) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw().subtract(totalPrice));
                saveWallet(walletBuyer);

                Contract contract = new Contract(request.getPackageId(), contractCode,
                        request.getRequirement(), request.getQuantity(), contractCancelFee,
                        serviceDeposit, totalPrice, countTotalDeliveryTime, expectCompleteDate,
                        null, OrderStatus.PENDING,null, ContractType.SERVICE, buyer, seller);
                Contract newContract = contractRepository.save(contract);

                String linkOrderForSeller = WebConstant.DOMAIN + "/dashboard/" + seller.getBrandName() + "/order/" + newContract.getId();
                String linkOrderForBuyer = WebConstant.DOMAIN + "/dashboard/" + currentUser.getId() + "/order/" + newContract.getId();
                try {
                    emailSender.sendEmailNotiContractSeller(seller.getUser().getEmail(),
                            seller.getUser().getLastName(), linkOrderForSeller,
                            totalPrice, request.getQuantity());

                    emailSender.sendEmailNotiContractBuyer(currentUser.getEmail(),
                            currentUser.getLastName(), linkOrderForBuyer,
                            totalPrice, request.getQuantity());
                } catch (UnsupportedEncodingException | MessagingException exception) {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "C?? l???i khi g???i khi g???i th??ng b??o t???i email c???a b???n");
                }

                return new ContractResponse(newContract.getId(), newContract.getPackageId(),
                        newContract.getContractCode(), newContract.getRequirement(),
                        newContract.getQuantity(), newContract.getContractCancelFee(),
                        newContract.getServiceDeposit(), newContract.getTotalPrice(),
                        newContract.getTotalDeliveryTime(), newContract.getExpectCompleteDate(),
                        null, OrderStatus.PENDING, null, null,
                        newContract.getBuyer().getUser().getId(),
                        newContract.getSeller().getUser().getId());
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Trong t??i kho???n hi???n kh??ng ????? ti???n ????? thanh to??n");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    //ACCEPT ORDER BY SELLER => OrderStatus.ACTIVE && DeliveryStatus.PROCESSING
    //When seller accept order
    @Override
    public ContractResponse updateStatusAcceptFromSeller(UUID id, UserPrincipal currentUser) throws RuntimeException {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found", currentUser.getId()));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found", id));
        Wallet walletSeller = walletRepository.findWalletByUserId(currentUser.getId());

        Date expectCompleteDate = dateDelivery.expectDate(Calendar.DAY_OF_MONTH, contract.getTotalDeliveryTime());
        BigDecimal serviceDeposit = contract.getServiceDeposit();

        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if (contract.getDeliveryStatus() != null && contract.getDeliveryStatus().equals(DeliveryStatus.PENDING)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "B???n ??ang trong qu?? tr??nh th???c hi???n h???p ?????ng");
            } else if(contract.getContractStatus() != null && contract.getContractStatus().equals(ContractStatus.CANCEL)
                    || contract.getOrderStatus() != null && contract.getOrderStatus().equals(OrderStatus.CANCEL)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Kh??ng th??? ti???p t???c th???c hi???n h???p ?????ng do b???n ho???c ng?????i b??n t??? ch???i");
            } else {
                if (walletSeller.getWithdraw().compareTo(serviceDeposit) >= 0) {
                    //Need min withdraw to accept contract
                    walletSeller.setWithdraw(walletSeller.getWithdraw().subtract(serviceDeposit));
                    contract.setDeliveryStatus(DeliveryStatus.PENDING);
                    contract.setOrderStatus(OrderStatus.TO_CONTRACT);
                    contract.setContractStatus(ContractStatus.PROCESSING);
                    //Khi accept order ng??y n??o th?? t??? ng??y hi???n t???i + totalDeliveryTime ra ng??y expectmpleteDate tren front-end
                    contract.setExpectCompleteDate(expectCompleteDate);
                    contract.setUpdatedAt(new Date());
                    Delivery delivery = new Delivery();
                    delivery.setContract(contract);
                    saveWallet(walletSeller);

                    String linkOrder = WebConstant.DOMAIN + "/dashboard/order/" + contract.getId();
                    try {
                        emailSender.sendEmailNotiAcceptContractToSeller(seller.getUser().getEmail(),
                                seller.getUser().getLastName(), linkOrder, contract.getContractCode(),
                                contract.getTotalPrice(), contract.getQuantity(), expectCompleteDate);

                        emailSender.sendEmailNotiAcceptContractToBuyer(contract.getBuyer().getUser().getEmail(),
                                contract.getBuyer().getUser().getLastName(), seller.getBrandName(), linkOrder,
                                contract.getContractCode(), contract.getTotalPrice(), contract.getQuantity());
                    } catch (UnsupportedEncodingException | MessagingException exception) {
                        throw new JovinnException(HttpStatus.BAD_REQUEST, "C?? l???i khi g???i khi g???i th??ng b??o t???i email c???a b???n");
                    }

                    return getUpdateResponse(contract, DeliveryStatus.PENDING, OrderStatus.TO_CONTRACT, ContractStatus.PROCESSING);
                } else {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "????? tham gia h???p ?????ng b???n c???n c?? m???t l?????ng JCoin ??t nh???t l??: " + contract.getServiceDeposit());
                }
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission to accept this order");
        throw new UnauthorizedException(apiResponse);
    }

    //REJECT ORDER BY SELLER => OrderStatus.CANCEL && DeliveryStatus.REJECT
    @Override
    public ContractResponse updateStatusRejectFromSeller(UUID id, UserPrincipal currentUser) throws RuntimeException {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found"));
        Seller seller = sellerRepository.findById(contract.getSeller().getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
        Wallet walletBuyer = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());
        BigDecimal serviceDepositRefund = calculateRefund90PercentDeposit(contract.getServiceDeposit(), 90);
        BigDecimal buyerReceiveWhenProcessing = contract.getTotalPrice().add(serviceDepositRefund);

        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if (contract.getContractStatus() != null && contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw().add(buyerReceiveWhenProcessing));
                contract.setContractStatus(ContractStatus.CANCEL);
            }  else if (contract.getContractStatus() != null && contract.getContractStatus().equals(ContractStatus.CANCEL)
                    || contract.getOrderStatus() != null && contract.getOrderStatus().equals(OrderStatus.CANCEL)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Kh??ng th??? t??? ch???i do h???p ?????ng ???? k???t th??c");
            } else {
                //Refund price for buyer not including serviceFee
                walletBuyer.setWithdraw(walletBuyer.getWithdraw().add(contract.getTotalPrice()));
                contract.setOrderStatus(OrderStatus.REJECT);
                contract.setContractStatus(null);
            }

            contract.setDeliveryStatus(null);
            contract.setUpdatedAt(new Date());
            contract.setSeller(seller);
            OrderStatus orderStatus = contract.getOrderStatus();
            ContractStatus contractStatus = contract.getContractStatus();

            saveWallet(walletBuyer);

            String linkOrder = WebConstant.DOMAIN + "/dashboard/order" + contract.getId();
            try {
                emailSender.sendEmailNotiRejectContractToSeller(seller.getUser().getEmail(),
                        seller.getUser().getLastName(), linkOrder, contract.getContractCode());

                emailSender.sendEmailNotiRejectContractToBuyer(contract.getBuyer().getUser().getEmail(),
                        contract.getBuyer().getUser().getLastName(), seller.getBrandName(), linkOrder,
                        contract.getContractCode(), contract.getTotalPrice());
            } catch (UnsupportedEncodingException | MessagingException exception) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "C?? l???i khi g???i th??ng b??o t???i email c???a b???n");
            }

            return getUpdateResponse(contract, null, orderStatus, contractStatus);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse updateStatusCancelFromBuyer(UUID id, UserPrincipal currentUser) throws RuntimeException {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found"));
        Wallet walletBuyer = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());
        Wallet walletSeller = walletRepository.findWalletByUserId(contract.getSeller().getUser().getId());

        BigDecimal refundDeposit = calculateRefund90PercentDeposit(contract.getServiceDeposit(), 90);
        BigDecimal buyerReceiveAfterCancel = contract.getTotalPrice().subtract(contract.getServiceDeposit());
        BigDecimal sellerReceiveAfterCancel = contract.getServiceDeposit().add(refundDeposit);

        if (contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            if (contract.getContractStatus() != null && contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw().add(buyerReceiveAfterCancel));
                walletSeller.setWithdraw(walletSeller.getWithdraw().add(sellerReceiveAfterCancel));
                contract.setContractStatus(ContractStatus.CANCEL);
            } else if (contract.getContractStatus() != null && contract.getContractStatus().equals(ContractStatus.CANCEL)
                    || contract.getOrderStatus() != null && contract.getOrderStatus().equals(OrderStatus.CANCEL)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Kh??ng th??? t??? ch???i do h???p ?????ng ???? k???t th??c");
            } else {
                walletBuyer.setWithdraw(walletBuyer.getWithdraw().add(contract.getTotalPrice()));
                contract.setOrderStatus(OrderStatus.CANCEL);
            }

            contract.setDeliveryStatus(null);
            contract.setUpdatedAt(new Date());
            contract.setBuyer(buyer);
            OrderStatus orderStatus = contract.getOrderStatus();
            ContractStatus contractStatus = contract.getContractStatus();

            saveWallet(walletBuyer);
            saveWallet(walletSeller);

            String linkOrder = WebConstant.DOMAIN + "/dashboard/order" + contract.getId();
            try {
                emailSender.sendEmailNotiRejectContractToSeller(contract.getSeller().getUser().getEmail(),
                        contract.getSeller().getUser().getLastName(), linkOrder, contract.getContractCode());

                emailSender.sendEmailNotiRejectContractToBuyer(contract.getBuyer().getUser().getEmail(),
                        contract.getBuyer().getUser().getLastName(), contract.getSeller().getBrandName(), linkOrder,
                        contract.getContractCode(), contract.getTotalPrice());
            } catch (UnsupportedEncodingException | MessagingException exception) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "C?? l???i khi g???i th??ng b??o t???i email c???a b???n");
            }

            return getUpdateResponse(contract, null, orderStatus, contractStatus);
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse updateStatusAcceptDeliveryFromBuyer(UUID id, UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found"));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found"));
        Wallet walletSeller = walletRepository.findWalletByUserId(contract.getSeller().getUser().getId());

        BigDecimal income = calculateRefund90PercentDeposit(contract.getTotalPrice(), 90);
        BigDecimal sellerReceiveAfterCancel = contract.getServiceDeposit().add(income);

        if (contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            if (contract.getDeliveryStatus() != null && contract.getDeliveryStatus().equals(DeliveryStatus.SENDING)
                && contract.getContractStatus() != null && !contract.getContractStatus().equals(ContractStatus.COMPLETE)) {
                if (contract.getPostRequest() != null && contract.getPostRequest().getMilestoneContracts() != null) {
                    boolean checkAllFinish = Boolean.FALSE;
                    List<MilestoneContract> milestoneContracts = contract.getPostRequest().getMilestoneContracts();
                    for(MilestoneContract milestoneContract : milestoneContracts) {
                        if(Boolean.TRUE.equals(deliveryRepository.existsByMilestoneId(milestoneContract.getId()))) {
                            checkAllFinish = Boolean.TRUE;
                        } else {
                            checkAllFinish = Boolean.FALSE;
                            break;
                        }
                    }

                    if(checkAllFinish) {
                        contract.setContractStatus(ContractStatus.COMPLETE);
                        contract.setUpdatedAt(new Date());
                        buyer.setSuccessContract(buyer.getSuccessContract() + 1);
                        buyerRepository.save(buyer);
                        Seller seller = contract.getSeller();
                        seller.setTotalOrderFinish(seller.getTotalOrderFinish() + 1);
                        sellerRepository.save(seller);
                        return getUpdateResponse(contract, DeliveryStatus.SENDING, OrderStatus.TO_CONTRACT, ContractStatus.COMPLETE);
                    } else {
                        throw new JovinnException(HttpStatus.BAD_REQUEST, "B???n ch??a ????nh d???u ho??n th??nh t???t c??? c??c milestone");
                    }
                } else {
                    walletSeller.setWithdraw(walletSeller.getWithdraw().add(sellerReceiveAfterCancel));
                    walletSeller.setIncome(walletSeller.getIncome().add(income));
                    contract.setContractStatus(ContractStatus.COMPLETE);
                    contract.setUpdatedAt(new Date());
                    saveWallet(walletSeller);
                    buyer.setSuccessContract(buyer.getSuccessContract() + 1);
                    buyerRepository.save(buyer);
                    Seller seller = contract.getSeller();
                    seller.setTotalOrderFinish(seller.getTotalOrderFinish() + 1);
                    sellerRepository.save(seller);
                    return getUpdateResponse(contract, DeliveryStatus.SENDING, OrderStatus.TO_CONTRACT, ContractStatus.COMPLETE);
                }

//                String linkOrder = WebConstant.DOMAIN + "/dashboard/order" + contract.getId();
//                try {
//                    emailSender.sendEmailNotiRejectContractToSeller(contract.getSeller().getUser().getEmail(),
//                            contract.getSeller().getUser().getLastName(), linkOrder, contract.getContractCode());
//
//                    emailSender.sendEmailNotiRejectContractToBuyer(contract.getBuyer().getUser().getEmail(),
//                            contract.getBuyer().getUser().getLastName(), contract.getSeller().getBrandName(), linkOrder,
//                            contract.getContractCode(), contract.getTotalPrice());
//                } catch (UnsupportedEncodingException | MessagingException exception) {
//                    throw new JovinnException(HttpStatus.BAD_REQUEST, "C?? l???i khi g???i th??ng b??o t???i email c???a b???n");
//                }

                //return getUpdateResponse(contract, DeliveryStatus.SENDING, OrderStatus.TO_CONTRACT, ContractStatus.COMPLETE);
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "???? x???y ra l???i khi ch???p nh???n b??n giao");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse acceptDeliveryForMilestone(UUID contractId, UUID milestoneId, UserPrincipal currentUser) {
        MilestoneContract milestoneContract = milestoneContractRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Milestone not found"));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found"));
        Wallet walletSeller = walletRepository.findWalletByUserId(contract.getSeller().getUser().getId());
        BigDecimal incomeMilestone = calculateRefund90PercentDeposit(milestoneContract.getMilestoneFee(), 90);
        if(contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            milestoneContract.setStatus(MilestoneStatus.COMPLETE);
            milestoneContractRepository.save(milestoneContract);
            walletSeller.setWithdraw(walletSeller.getWithdraw().add(incomeMilestone));
            walletSeller.setIncome(walletSeller.getIncome().add(incomeMilestone));
            saveWallet(walletSeller);
            return new ApiResponse(Boolean.TRUE, "B???n ???? nh???n v?? thanh to??n m?? giai ??o???n n??y th??nh c??ng");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse createContractFromSellerOffer(UUID offerRequestId, UserPrincipal currentUser) {
        OfferRequest offerRequest = offerRequestRepository.findById(offerRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Kh??ng t??m th???y offerRequest"));
        PostRequest postRequest = postRequestRepository.findPostRequestById(offerRequest.getPostRequest().getId());
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found "));
        Seller seller = sellerRepository.findById(offerRequest.getSeller().getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found "));
        Wallet walletBuyer = walletRepository.findWalletByUserId(postRequest.getUser().getId());

        String contractCode = getRandomContractNumber();
        Integer countTotalDeliveryTime = offerRequest.getTotalDeliveryTime();
        Date expectCompleteDate = dateDelivery.expectDate(Calendar.DAY_OF_MONTH, countTotalDeliveryTime);
        BigDecimal totalPrice = offerRequest.getOfferPrice();
        BigDecimal serviceDeposit = totalPrice.multiply(new BigDecimal(offerRequest.getCancelFee())).divide(ONE_HUNDRED, RoundingMode.FLOOR);

        if (postRequest.getUser().getId().equals(currentUser.getId())) {
            if (walletBuyer.getWithdraw().compareTo(totalPrice) >= 0 ) {
                if(offerRequest.getOfferRequestStatus().equals(OfferRequestStatus.ACCEPTED)) {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Offer ???? ???????c ch???p nh???n");
                } else {
                    Contract contract = new Contract(null, contractCode,
                            postRequest.getShortRequirement(), 1, offerRequest.getCancelFee(),
                            serviceDeposit, totalPrice, countTotalDeliveryTime, expectCompleteDate,
                            DeliveryStatus.PENDING, OrderStatus.TO_CONTRACT, ContractStatus.PROCESSING,
                            ContractType.REQUEST, buyer, seller);
                    if (postRequest.getMilestoneContracts() != null) {
                        List<MilestoneContract> milestoneContracts = milestoneContractRepository.findAllByPostRequestId(postRequest.getId());
                        for(MilestoneContract milestoneContract : milestoneContracts) {
                            milestoneContract.setStatus(MilestoneStatus.PROCESSING);
                        }
                    }
                    contract.setPostRequest(postRequest);
                    Contract newContract = contractRepository.save(contract);

                    offerRequest.setOfferRequestStatus(OfferRequestStatus.ACCEPTED);
                    offerRequestRepository.save(offerRequest);

                    walletBuyer.setWithdraw(walletBuyer.getWithdraw().subtract(totalPrice));
                    saveWallet(walletBuyer);

                    updateStatusAfterAcceptOffer(postRequest, offerRequest);
                    changeStatusAllOfferRejected(postRequest);
                    return new ContractResponse(newContract.getId(), newContract.getPackageId(),
                            newContract.getContractCode(), newContract.getRequirement(),
                            newContract.getQuantity(), newContract.getContractCancelFee(),
                            newContract.getServiceDeposit(), newContract.getTotalPrice(),
                            newContract.getTotalDeliveryTime(), newContract.getExpectCompleteDate(),
                            DeliveryStatus.PENDING, OrderStatus.TO_CONTRACT, ContractStatus.PROCESSING, postRequest,
                            newContract.getBuyer().getUser().getId(),
                            newContract.getSeller().getUser().getId());
                }
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Kh??ng ????? s??? d?? ????? b???t ?????u h???p ?????ng");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse createContractFromSellerApply(UUID postRequestId, UUID sellerId, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Kh??ng t??m th???y postRequest"));
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found"));
        Wallet walletBuyer = walletRepository.findWalletByUserId(postRequest.getUser().getId());

        String contractCode = getRandomContractNumber();
        BigDecimal totalPrice = postRequest.getBudget();
        Integer countTotalDeliveryTime = postRequest.getTotalDeliveryTime();
        Date expectCompleteDate = dateDelivery.expectDate(Calendar.DAY_OF_MONTH, countTotalDeliveryTime);
        BigDecimal serviceDeposit = postRequest.getBudget()
                .multiply(new BigDecimal(postRequest.getContractCancelFee()))
                .divide(ONE_HUNDRED, RoundingMode.FLOOR);

        if (postRequest.getUser().getId().equals(currentUser.getId())) {
            if (walletBuyer.getWithdraw().compareTo(totalPrice) >= 0 ) {
                Contract contract = new Contract(null, contractCode,
                        postRequest.getShortRequirement(), 1, postRequest.getContractCancelFee(),
                        serviceDeposit, totalPrice, countTotalDeliveryTime, expectCompleteDate,
                        DeliveryStatus.PENDING, OrderStatus.TO_CONTRACT, ContractStatus.PROCESSING,
                        ContractType.REQUEST, buyer, seller);
                if (postRequest.getMilestoneContracts() != null) {
                    List<MilestoneContract> milestoneContracts = milestoneContractRepository.findAllByPostRequestId(postRequest.getId());
                    for(MilestoneContract milestoneContract : milestoneContracts) {
                        milestoneContract.setStatus(MilestoneStatus.PROCESSING);
                    }
                }
                contract.setPostRequest(postRequest);
                Contract newContract = contractRepository.save(contract);

                walletBuyer.setWithdraw(walletBuyer.getWithdraw().subtract(totalPrice));
                saveWallet(walletBuyer);

                updateStatusPostRequestAfterAccepted(postRequest);
                changeStatusAllOfferRejected(postRequest);
                return new ContractResponse(newContract.getId(), newContract.getPackageId(),
                        newContract.getContractCode(), newContract.getRequirement(),
                        newContract.getQuantity(), newContract.getContractCancelFee(),
                        newContract.getServiceDeposit(), newContract.getTotalPrice(),
                        newContract.getTotalDeliveryTime(), newContract.getExpectCompleteDate(),
                        DeliveryStatus.PENDING, OrderStatus.TO_CONTRACT, ContractStatus.PROCESSING, postRequest,
                        newContract.getBuyer().getUser().getId(),
                        newContract.getSeller().getUser().getId());
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Kh??ng ????? s??? d?? ????? b???t ?????u h???p ?????ng");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Contract getContractById(UUID id, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found ", id));
        if (contract.getBuyer().getUser().getId().equals(currentUser.getId())
            || contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            return contract;
        } else {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "You don't have permission");
        }
    }

    @Override
    public List<Contract> getContractByStatus(ContractStatus status, UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found"));
        if(buyer.getUser().getSeller() == null) {
            return contractRepository.findAllByContractStatusAndBuyerId(status, buyer.getId());
        } else if(buyer.getUser().getSeller() != null) {
            Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
            return contractRepository.findAllByContractStatusAndSellerIdOrBuyerId(status, seller.getId(), buyer.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<Contract> getOrders(UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found"));

        if(buyer.getUser().getSeller() == null) {
            return contractRepository.findAllByOrderStatusAndBuyerId(OrderStatus.PENDING, buyer.getId());
        } else if(buyer.getUser().getSeller() != null) {
            Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
            return contractRepository.findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus.PENDING, buyer.getId(), seller.getId());
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<Contract> getContracts(UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found"));

        if(buyer.getUser().getSeller() == null) {
            return contractRepository.findAllByOrderStatusAndBuyerId(OrderStatus.TO_CONTRACT, buyer.getId());
        } else if(buyer.getUser().getSeller() != null) {
            Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
            return contractRepository.findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus.TO_CONTRACT, buyer.getId(), seller.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    private ContractResponse getUpdateResponse(Contract contract, DeliveryStatus deliveryStatus,
                                               OrderStatus orderStatus, ContractStatus contractStatus) {
        Contract update = contractRepository.save(contract);
        return new ContractResponse(update.getId(), update.getPackageId(),
                update.getContractCode(), update.getRequirement(), update.getQuantity(), update.getContractCancelFee(),
                update.getServiceDeposit(), update.getTotalPrice(),
                update.getTotalDeliveryTime(), update.getExpectCompleteDate(),
                deliveryStatus, orderStatus, contractStatus, null,
                update.getBuyer().getUser().getId(),
                update.getSeller().getUser().getId());
    }

    private void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }

    private static BigDecimal calculateRefund90PercentDeposit(BigDecimal serviceDeposit, Integer percent){
        if (percent % 5 == 0) {
            return serviceDeposit.multiply(new BigDecimal(percent)).divide(ONE_HUNDRED, RoundingMode.FLOOR);
        } else {
            throw new JovinnException(HttpStatus.BAD_REQUEST, "Percent need % 5");
        }
    }

    private static BigDecimal scale2(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_EVEN);
    }

    private void updateStatusAfterAcceptOffer(PostRequest postRequest, OfferRequest offerRequest) {
        offerRequest.setOfferRequestStatus(OfferRequestStatus.ACCEPTED);
        offerRequest.setUpdatedAt(new Date());
        offerRequestRepository.save(offerRequest);
        updateStatusPostRequestAfterAccepted(postRequest);
    }

    private void updateStatusPostRequestAfterAccepted(PostRequest postRequest) {
        postRequest.setStatus(PostRequestStatus.CLOSE);
        postRequest.setUpdatedAt(new Date());
        postRequestRepository.save(postRequest);
    }

    private void changeStatusAllOfferRejected(PostRequest postRequest) {
        List<OfferRequest> listOfferRequest = offerRequestRepository.findAllByPostRequestId(postRequest.getId());
        for(OfferRequest offerRequest : listOfferRequest) {
            if(!offerRequest.getOfferRequestStatus().equals(OfferRequestStatus.ACCEPTED)) {
                offerRequest.setOfferRequestStatus(OfferRequestStatus.REJECTED);
                offerRequestRepository.save(offerRequest);
                Notification notification = new Notification();
                notification.setUser(offerRequest.getSeller().getUser());
                notification.setLink("Link t???i post request m???i");
                notification.setShortContent("B???n ???? b??? t??? ch???i " + offerRequest.getId()
                        + " do b??i ????ng ???? ???????c k?? k???t h???p ?????ng."
                        + " ?????ng lo l???ng! H??y c??? g???ng t??m ???????c vi???c l??m ung ?? t???i ????y");
                notificationRepository.save(notification);
            }
        }

        List<Seller> listSellerApply = sellerRepository.findAllByPostRequests_Id(postRequest.getId());
        for(Seller seller : listSellerApply) {
            Notification notification = new Notification();
            notification.setUser(seller.getUser());
            notification.setLink("Link t???i post request m???i");
            notification.setShortContent("B???n ???? b??? t??? ch???i b??i ????ng c???a "
                    + postRequest.getUser().getLastName() + postRequest.getUser().getFirstName()
                    + " do ???? ???????c k?? k???t h???p ?????ng."
                    + " ?????ng lo l???ng! H??y c??? g???ng t??m ???????c vi???c l??m ung ?? t???i ????y");
            notificationRepository.save(notification);
        }
    }
}
