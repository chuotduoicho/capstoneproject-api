package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.ExtraOfferRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.ExtraOffer;
import com.jovinn.capstoneproject.model.Notification;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.repository.ExtraOfferRepository;
import com.jovinn.capstoneproject.repository.NotificationRepository;
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

    @Override
    public ApiResponse createExtraOffer(UUID contractId, ExtraOfferRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        if(contract.getBuyer().getUser().getId().equals(currentUser.getId())) {
            if(contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
                ExtraOffer extraOffer = new ExtraOffer();
                extraOffer.setTitle(request.getTitle());
                extraOffer.setShortDescription(request.getShortDescription());
                extraOffer.setExtraPrice(request.getExtraPrice());
                extraOffer.setAdditionTime(request.getAdditionTime());
                extraOffer.setOpened(Boolean.TRUE);
                extraOffer.setContract(contract);
                ExtraOffer save = extraOfferRepository.save(extraOffer);
                sendNotification(WebConstant.DOMAIN + "/sellerHome/manageContract/" + contractId,
                        "Bạn nhận được lời đề nghị mới cho hợp đồng với giá " + extraOffer.getExtraPrice(), contract.getSeller().getUser());
                return new ApiResponse(Boolean.TRUE, "Bạn đã gửi lời đề nghị cho hợp đồng thành công với mức giá là " + save.getExtraPrice());
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
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy extra offer"));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy hợp đồng"));
        if(contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if(contract.getContractStatus().equals(ContractStatus.PROCESSING) && extraOffer.getOpened().equals(Boolean.TRUE)) {
                Date completeExpectDateWithExtra = dateDelivery.expectDate(contract.getTotalDeliveryTime(), extraOffer.getAdditionTime());
                contract.setTotalPrice(contract.getTotalPrice().add(extraOffer.getExtraPrice()));
                contract.setExpectCompleteDate(completeExpectDateWithExtra);
                contract.setTotalDeliveryTime(contract.getTotalDeliveryTime() + extraOffer.getAdditionTime());
                contract.setServiceDeposit(contract.getServiceDeposit().add(
                        extraOffer.getExtraPrice().multiply(
                                new BigDecimal(10)).divide(new BigDecimal(100), RoundingMode.FLOOR)));
                contractRepository.save(contract);
                extraOffer.setOpened(Boolean.FALSE);
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
            if(extraOffer.getOpened().equals(Boolean.TRUE)) {
                extraOffer.setOpened(Boolean.FALSE);
                extraOfferRepository.save(extraOffer);
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
}
