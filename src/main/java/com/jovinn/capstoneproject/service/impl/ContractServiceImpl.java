package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.adminsite.adminresponse.AdminViewContractsResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountContractResponse;
import com.jovinn.capstoneproject.dto.adminsite.adminresponse.CountTotalRevenueResponse;
import com.jovinn.capstoneproject.dto.client.request.ContractRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.AvatarResponse;
import com.jovinn.capstoneproject.dto.client.response.ContractResponse;
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
import java.util.*;

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
                        null, OrderStatus.PENDING,null, ContractType.SERVICE, buyer, seller, Boolean.FALSE);
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
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi khi gửi thông báo tới email của bạn");
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Trong tài khoản hiện không đủ tiền để thanh toán");
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn đang trong quá trình thực hiện hợp đồng");
            } else if(contract.getContractStatus() != null && contract.getContractStatus().equals(ContractStatus.CANCEL)
                    || contract.getOrderStatus() != null && contract.getOrderStatus().equals(OrderStatus.CANCEL)) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể tiếp tục thực hiện hợp đồng do bạn hoặc người bán từ chối");
            } else {
                if (walletSeller.getWithdraw().compareTo(serviceDeposit) >= 0) {
                    //Need min withdraw to accept contract
                    walletSeller.setWithdraw(walletSeller.getWithdraw().subtract(serviceDeposit));
                    contract.setDeliveryStatus(DeliveryStatus.PENDING);
                    contract.setOrderStatus(OrderStatus.TO_CONTRACT);
                    contract.setContractStatus(ContractStatus.PROCESSING);
                    //Khi accept order ngày nào thì từ ngày hiện tại + totalDeliveryTime ra ngày expectmpleteDate tren front-end
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
                        throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi khi gửi thông báo tới email của bạn");
                    }

                    return getUpdateResponse(contract, DeliveryStatus.PENDING, OrderStatus.TO_CONTRACT, ContractStatus.PROCESSING);
                } else {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Để tham gia hợp đồng bạn cần có một lượng JCoin ít nhất là: " + contract.getServiceDeposit());
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể từ chối do hợp đồng đã kết thúc");
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi thông báo tới email của bạn");
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể từ chối do hợp đồng đã kết thúc");
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi thông báo tới email của bạn");
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
                        throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn chưa đánh dấu hoàn thành tất cả các milestone");
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
//                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Có lỗi khi gửi thông báo tới email của bạn");
//                }

                //return getUpdateResponse(contract, DeliveryStatus.SENDING, OrderStatus.TO_CONTRACT, ContractStatus.COMPLETE);
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Đã xảy ra lỗi khi chấp nhận bàn giao");
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
            return new ApiResponse(Boolean.TRUE, "Bạn đã nhận và thanh toán mã giai đoạn này thành công");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse createContractFromSellerOffer(UUID offerRequestId, UserPrincipal currentUser) {
        OfferRequest offerRequest = offerRequestRepository.findById(offerRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy offerRequest"));
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
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Offer đã được chấp nhận");
                } else {
                    Contract contract = new Contract(null, contractCode,
                            postRequest.getShortRequirement(), 1, offerRequest.getCancelFee(),
                            serviceDeposit, totalPrice, countTotalDeliveryTime, expectCompleteDate,
                            DeliveryStatus.PENDING, OrderStatus.TO_CONTRACT, ContractStatus.PROCESSING,
                            ContractType.REQUEST, buyer, seller, Boolean.FALSE);
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không đủ số dư để bắt đầu hợp đồng");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ContractResponse createContractFromSellerApply(UUID postRequestId, UUID sellerId, UserPrincipal currentUser) {
        PostRequest postRequest = postRequestRepository.findById(postRequestId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy postRequest"));
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
                        ContractType.REQUEST, buyer, seller, Boolean.FALSE);
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
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không đủ số dư để bắt đầu hợp đồng");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse flagNotAcceptDelivery(UUID contractId, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        boolean checkAllFinish = Boolean.FALSE;
        if(contract.getBuyer().getId().equals(currentUser.getId())) {
            if(contract.getPostRequest().getMilestoneContracts() != null) {
                List<MilestoneContract> milestoneContracts = milestoneContractRepository.findAllByPostRequestId(contract.getPostRequest().getId());
                for(MilestoneContract milestoneContract : milestoneContracts) {
                    if(milestoneContract.getStatus().equals(MilestoneStatus.COMPLETE)) {
                        checkAllFinish = Boolean.TRUE;
                    } else {
                        checkAllFinish = Boolean.FALSE;
                        break;
                    }
                }

                if(!checkAllFinish) {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể đánh cờ do chưa hoàn thành hết các bàn giao");
                }
            }

            Delivery delivery = deliveryRepository.findByContractId(contractId);
            if(delivery == null || !checkAllFinish) {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể cắm cờ vì chưa có bàn giao tải lên từ phía người bán" +
                                                                " hoặc các giai đoạn chưa được hoàn thành");
            } else {
                Date autoCompleteExpectDate = dateDelivery.expectDate(delivery.getCreateAt().getDay(), 3);
                if(contract.getDeliveryStatus().equals(DeliveryStatus.SENDING) &&
                        contract.getContractStatus().equals(ContractStatus.PROCESSING) &&
                        autoCompleteExpectDate.compareTo(new Date()) < 0) {
                    contract.setFlag(Boolean.TRUE);
                    contractRepository.save(contract);
                    return new ApiResponse(Boolean.TRUE, "Bạn đã đặt cờ từ chối bàn giao thành công");
                } else {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Bạn không thể đặt cờ từ chối bàn giao tự động " +
                            "do đã hệ thống đã chấp nhận tự động hoặc đã hoàn thành hợp đồng này ");
                }
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public Contract getContractById(UUID id, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
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
        //Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);

        if(buyer.getUser().getSeller() == null) {
//            Page<Contract> contracts = contractRepository.findAllByContractStatusAndBuyerId(status, buyer.getId(), pageable);
//            List<Contract> content = contracts.getNumberOfElements() == 0 ? Collections.emptyList() : contracts.getContent();
//            return new PageResponse<>(content, contracts.getNumber(), contracts.getSize(), contracts.getTotalElements(),
//                    contracts.getTotalPages(), contracts.isLast());
            return contractRepository.findAllByContractStatusAndBuyerId(status, buyer.getId());
        } else if(buyer.getUser().getSeller() != null) {
            Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
//            Page<Contract> contracts = contractRepository.findAllByContractStatusAndSellerIdOrBuyerId(status, seller.getId(), buyer.getId(), pageable);
//            List<Contract> content = contracts.getNumberOfElements() == 0 ? Collections.emptyList() : contracts.getContent();
//            return new PageResponse<>(content, contracts.getNumber(), contracts.getSize(), contracts.getTotalElements(),
//                    contracts.getTotalPages(), contracts.isLast());
            return contractRepository.findAllByContractStatusAndSellerIdOrBuyerId(status, seller.getId(), buyer.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<Contract> getOrders(UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found"));
        //Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);

        if(buyer.getUser().getSeller() == null) {
//            Page<Contract> contracts = contractRepository.findAllByOrderStatusAndBuyerId(OrderStatus.PENDING, buyer.getId(), pageable);
//            List<Contract> content = contracts.getNumberOfElements() == 0 ? Collections.emptyList() : contracts.getContent();
//            return new PageResponse<>(content, contracts.getNumber(), contracts.getSize(), contracts.getTotalElements(),
//                    contracts.getTotalPages(), contracts.isLast());
            return contractRepository.findAllByOrderStatusAndBuyerId(OrderStatus.PENDING, buyer.getId());
        } else if(buyer.getUser().getSeller() != null) {
            Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
//            Page<Contract> contracts = contractRepository.findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus.PENDING, buyer.getId(), seller.getId(), pageable);
//            List<Contract> content = contracts.getNumberOfElements() == 0 ? Collections.emptyList() : contracts.getContent();
//            return new PageResponse<>(content, contracts.getNumber(), contracts.getSize(), contracts.getTotalElements(),
//                    contracts.getTotalPages(), contracts.isLast());
            return contractRepository.findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus.PENDING, buyer.getId(), seller.getId());
        }
        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public List<Contract> getContracts(UserPrincipal currentUser) {
        Buyer buyer = buyerRepository.findBuyerByUserId(currentUser.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Buyer not found"));
        //Pageable pageable = Pagination.paginationCommon(page, size, sortBy, sortDir);
        if(buyer.getUser().getSeller() == null) {
//            Page<Contract> contracts = contractRepository.findAllByOrderStatusAndBuyerId(OrderStatus.TO_CONTRACT, buyer.getId(), pageable);
//            List<Contract> content = contracts.getNumberOfElements() == 0 ? Collections.emptyList() : contracts.getContent();
//            return new PageResponse<>(content, contracts.getNumber(), contracts.getSize(), contracts.getTotalElements(),
//                    contracts.getTotalPages(), contracts.isLast());
            return contractRepository.findAllByOrderStatusAndBuyerId(OrderStatus.TO_CONTRACT, buyer.getId());
        } else if(buyer.getUser().getSeller() != null) {
            Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Seller not found"));
//            Page<Contract> contracts = contractRepository.findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus.TO_CONTRACT, buyer.getId(), seller.getId(), pageable);
//            List<Contract> content = contracts.getNumberOfElements() == 0 ? Collections.emptyList() : contracts.getContent();
//            return new PageResponse<>(content, contracts.getNumber(), contracts.getSize(), contracts.getTotalElements(),
//                    contracts.getTotalPages(), contracts.isLast());
            return contractRepository.findAllByOrderStatusAndBuyerIdOrSellerId(OrderStatus.TO_CONTRACT, buyer.getId(), seller.getId());
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse autoCheckCompleteContract() {
        List<Contract>  contracts = contractRepository.findAllByContractStatus(ContractStatus.PROCESSING);
        List<Contract> list = new ArrayList<>();
        for(Contract contract : contracts) {
            Delivery delivery = deliveryRepository.findByContractId(contract.getId());
            if(delivery != null) {
                Date autoCompleteExpectDate = dateDelivery.expectDate(delivery.getCreateAt().getDay(), 3);
                if(contract.getDeliveryStatus().equals(DeliveryStatus.SENDING) &&
                        autoCompleteExpectDate.compareTo(new Date()) > 0) {
                    contract.setContractStatus(ContractStatus.COMPLETE);
                    contractRepository.save(contract);
                    list.add(contract);
                }
            }
        }
        return new ApiResponse(Boolean.TRUE, "Đã thay đổi trạng thái của " + list.size() + " hợp đồng");
    }

    @Override
    public AvatarResponse getAvatarBoth(UUID contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        return new AvatarResponse(contract.getBuyer().getUser().getAvatar(), contract.getSeller().getUser().getAvatar());
    }

    @Override
    public CountTotalRevenueResponse getTotalRevenue() {
//        List<Contract> contracts = contractRepository.findAllByContractStatus(ContractStatus.COMPLETE);
//        BigDecimal totalRevenue = new BigDecimal(0);
//        BigDecimal number = new BigDecimal(0.1);
//        for (Contract contract:contracts){
//            totalRevenue.add(contract.getTotalPrice());
//        }
        return new CountTotalRevenueResponse(contractRepository.countTotalRevenue());
    }

    @Override
    public CountContractResponse countTotalContractByCatId(UUID catId) {
        return new CountContractResponse(contractRepository.countContractByPostRequest_Category_Id(catId));
    }

    @Override
    public List<AdminViewContractsResponse> getContractsByCategoryId(UUID catId) {
        List<Contract> contracts = contractRepository.findAllByPostRequest_Category_Id(catId);
        List<AdminViewContractsResponse> contractResponses = new ArrayList<>();
        for (Contract newContract:
             contracts) {
            contractResponses.add(new AdminViewContractsResponse(newContract.getContractCode(),
                    newContract.getPostRequest().getUser().getUsername(),
                    newContract.getPostRequest().getUser().getFirstName()+" "+
                            newContract.getPostRequest().getUser().getLastName(),newContract.getTotalPrice(),
                    newContract.getCreateAt()));
        }
        return contractResponses;
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
                notification.setUnread(Boolean.TRUE);
                notification.setLink("Link tới post request mới");
                notification.setShortContent("Bạn đã bị từ chối " + offerRequest.getId()
                        + " do bài đăng đã được ký kết hợp đồng."
                        + " Đừng lo lắng! Hãy cố gắng tìm được việc làm ung ý tại đây");
                notificationRepository.save(notification);
            }
        }

        List<Seller> listSellerApply = sellerRepository.findAllByPostRequests_Id(postRequest.getId());
        for(Seller seller : listSellerApply) {
            Notification notification = new Notification();
            notification.setUser(seller.getUser());
            notification.setUnread(Boolean.TRUE);
            notification.setLink("Link tới post request mới");
            notification.setShortContent("Bạn đã bị từ chối bài đăng của "
                    + postRequest.getUser().getLastName() + postRequest.getUser().getFirstName()
                    + " do đã được ký kết hợp đồng."
                    + " Đừng lo lắng! Hãy cố gắng tìm được việc làm ung ý tại đây");
            notificationRepository.save(notification);
        }
    }
}
