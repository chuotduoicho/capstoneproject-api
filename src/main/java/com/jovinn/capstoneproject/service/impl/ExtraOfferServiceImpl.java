package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.ExtraOfferRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.ExtraOfferStatus;
import com.jovinn.capstoneproject.enumerable.TransactionType;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.*;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.repository.ExtraOfferRepository;
import com.jovinn.capstoneproject.repository.NotificationRepository;
import com.jovinn.capstoneproject.repository.payment.TransactionRepository;
import com.jovinn.capstoneproject.repository.payment.WalletRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.ExtraOfferService;
import com.jovinn.capstoneproject.util.DateDelivery;
import com.jovinn.capstoneproject.util.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;

@Service
public class ExtraOfferServiceImpl implements ExtraOfferService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ExtraOfferRepository extraOfferRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private DateDelivery dateDelivery;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public ApiResponse createExtraOffer(UUID contractId, ExtraOfferRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        if(contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            if(contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
                Wallet walletBuyer = walletRepository.findWalletByUserId(currentUser.getId());
                if(walletBuyer.getWithdraw().compareTo(request.getExtraPrice()) >= 0) {
                    ExtraOffer extraOffer = new ExtraOffer();
                    extraOffer.setTitle(request.getTitle());
                    extraOffer.setShortDescription(request.getShortDescription());
                    extraOffer.setExtraPrice(request.getExtraPrice());
                    extraOffer.setAdditionTime(request.getAdditionTime());
                    extraOffer.setOpened(Boolean.TRUE);
                    extraOffer.setStatus(ExtraOfferStatus.PENDING);
                    extraOffer.setContract(contract);
                    ExtraOffer save = extraOfferRepository.save(extraOffer);
                    walletBuyer.setWithdraw(walletBuyer.getWithdraw().subtract(extraOffer.getExtraPrice()));
                    walletRepository.save(walletBuyer);
                    createSpendTransaction(currentUser.getId(), walletBuyer, extraOffer.getExtraPrice(),
                            "EXTRA-" + contract.getContractCode(),
                            "Bạn đã gửi thêm đề nghị mở rộng");
                    sendNotification(WebConstant.DOMAIN + "/sellerHome/manageContract/" + contractId,
                            "Bạn nhận được lời đề nghị mới cho hợp đồng với giá " + extraOffer.getExtraPrice() + "$", contract.getSeller().getUser());
                    return new ApiResponse(Boolean.TRUE, "Bạn đã gửi lời đề nghị cho hợp đồng thành công với mức giá là " + save.getExtraPrice());
                } else {
                    throw new JovinnException(HttpStatus.BAD_REQUEST, "Trong ví của bạn hiện tại không đủ để thanh toán");
                }
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể thêm lời đề nghị mới do hợp đồng đã hoàn thành hoặc bị hủy");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse sellerAcceptExtraOffer(UUID contractId, UUID extraOfferId, UserPrincipal currentUser) {
        ExtraOffer extraOffer = extraOfferRepository.findById(extraOfferId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy lời đề nghị mở rộng"));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        if(contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if(contract.getContractStatus().equals(ContractStatus.PROCESSING)
                    && extraOffer.getOpened().equals(Boolean.TRUE)
                    && extraOffer.getStatus().equals(ExtraOfferStatus.PENDING)) {
                Date completeExpectDateWithExtra = dateDelivery.expectDateCompleteAuto(contract.getExpectCompleteDate(), extraOffer.getAdditionTime());
                contract.setTotalPrice(contract.getTotalPrice().add(extraOffer.getExtraPrice()));
                contract.setExpectCompleteDate(completeExpectDateWithExtra);
                contract.setTotalDeliveryTime(contract.getTotalDeliveryTime() + extraOffer.getAdditionTime());
                contract.setServiceDeposit(contract.getServiceDeposit().add(
                        extraOffer.getExtraPrice().multiply(
                                new BigDecimal(10)).divide(new BigDecimal(100), RoundingMode.FLOOR)));
                extraOffer.setStatus(ExtraOfferStatus.ACCEPT);
                contractRepository.save(contract);
                extraOfferRepository.save(extraOffer);
                return new ApiResponse(Boolean.TRUE, "Bạn đã tiếp nhận lời đề nghị thành công");
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể nhận lời đề nghị mới do hợp đồng đã hoàn thành hoặc bị hủy");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public ApiResponse cancelExtraOffer(UUID contractId, UUID extraOfferId, UserPrincipal currentUser) {
        ExtraOffer extraOffer = extraOfferRepository.findById(extraOfferId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy extra offer"));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        if(contract.getSeller().getUser().getId().equals(currentUser.getId()) || contract.getBuyer().getUser().getId().equals(currentUser.getId()) ) {
            if(contract.getContractStatus().equals(ContractStatus.PROCESSING) &&
                    extraOffer.getOpened().equals(Boolean.TRUE) && extraOffer.getStatus().equals(ExtraOfferStatus.PENDING)) {
                Wallet walletBuyer = walletRepository.findWalletByUserId(contract.getBuyer().getUser().getId());
                extraOffer.setOpened(Boolean.FALSE);
                extraOffer.setStatus(ExtraOfferStatus.REJECT);
                extraOfferRepository.save(extraOffer);
                walletBuyer.setWithdraw(walletBuyer.getWithdraw().add(extraOffer.getExtraPrice()));
                walletRepository.save(walletBuyer);
                sendNotification(WebConstant.DOMAIN + "/sellerHome/manageContract/" + contractId,
                        "Lời đề nghị đã bị hủy " + extraOffer.getExtraPrice() + "$", contract.getSeller().getUser());
                sendNotification(WebConstant.DOMAIN + "/buyerHome/manageContract/" + contractId,
                        "Lời đề nghị đã bị hủy " + extraOffer.getExtraPrice() + "$", contract.getBuyer().getUser());
                createTakeTransaction(walletBuyer.getUser().getId(), walletBuyer, extraOffer.getExtraPrice(),
                        "REJECT-EXTRA-" + contract.getContractCode(),
                        "Bạn được hoàn lại tiền thêm đề nghị mở rộng");
                return new ApiResponse(Boolean.TRUE, "Bạn đã hủy lời đề nghị với mức giá " + extraOffer.getExtraPrice());
            } else {
                throw new JovinnException(HttpStatus.BAD_REQUEST, "Không thể nhận lời đề nghị mới do hợp đồng đã hoàn thành hoặc bị hủy");
            }
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    private void sendNotification(String link, String shortContent, User user) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setShortContent(shortContent);
        notification.setLink(link);
        notification.setUnread(Boolean.TRUE);
        notificationRepository.save(notification);
    }

    private void createSpendTransaction(UUID userId, Wallet wallet, BigDecimal amount,
                                        String paymentCode, String message) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setCurrency("USD");
        transaction.setType(TransactionType.SPEND);
        transaction.setPaymentCode(paymentCode);
        transaction.setMessage(message);
        transactionRepository.save(transaction);
    }

    private void createTakeTransaction(UUID userId, Wallet wallet, BigDecimal amount,
                                       String paymentCode, String message) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setCurrency("USD");
        transaction.setType(TransactionType.TAKE);
        transaction.setPaymentCode(paymentCode);
        transaction.setMessage(message);
        transactionRepository.save(transaction);
    }
}
