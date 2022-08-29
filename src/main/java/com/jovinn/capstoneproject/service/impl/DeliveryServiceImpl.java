package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.client.request.DeliveryHaveMilestoneRequest;
import com.jovinn.capstoneproject.dto.client.request.DeliveryNotMilestoneRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.DeliveryHaveMilestoneResponse;
import com.jovinn.capstoneproject.dto.client.response.DeliveryNotMilestoneResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.enumerable.MilestoneStatus;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.JovinnException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.Delivery;
import com.jovinn.capstoneproject.model.MilestoneContract;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.repository.DeliveryRepository;
import com.jovinn.capstoneproject.repository.MilestoneContractRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.DeliveryService;
import com.jovinn.capstoneproject.util.PushNotification;
import com.jovinn.capstoneproject.util.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private PushNotification pushNotification;
    @Autowired
    private MilestoneContractRepository milestoneContractRepository;

    @Override
    public DeliveryNotMilestoneResponse createDelivery(UUID id, DeliveryNotMilestoneRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm tháy hợp đồng"));
        if (contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
            if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
                Delivery delivery = new Delivery(request.getFile(), request.getDescription(), contract);
                contract.setDeliveryStatus(DeliveryStatus.SENDING);
                contractRepository.save(contract);
                Delivery update = deliveryRepository.save(delivery);
                pushNotification.sendNotification(contract.getBuyer().getUser(),
                        WebConstant.DOMAIN + "/buyerhome/manageContract/" + contract.getId(),
                        "Bên đối tác đã tải lên bàn giao cho " + contract.getContractCode() + ", Kiếm tra ngay!");
//                if (contract.getExpectCompleteDate().compareTo(delivery.getCreateAt()) < 0) {
//                    contract.setDeliveryStatus(DeliveryStatus.LATE);
//                    contractRepository.save(contract);
//                }
                return new DeliveryNotMilestoneResponse(update.getId(), update.getContract().getId(),
                        update.getFile(), update.getDescription());
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bạn không thể bàn giao sản phẩm");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public DeliveryHaveMilestoneResponse createDeliveryMilestone(UUID contractId, DeliveryHaveMilestoneRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm tháy hợp đồng"));
        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            if (contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
                Delivery delivery = new Delivery(request.getFile(), request.getDescription(), contract);
                delivery.setMilestoneId(request.getMilestoneId());
                Delivery update = deliveryRepository.save(delivery);
                MilestoneContract milestoneContract = milestoneContractRepository.findById(request.getMilestoneId())
                        .orElseThrow(() -> new JovinnException(HttpStatus.BAD_REQUEST, "Không tìm thấy giai đoạn"));
                milestoneContract.setStatus(MilestoneStatus.SENDING);
                //Check if delivery for milestone is the last will update DeliveryStatus.SENDING for contract
                boolean checkAllComplete = Boolean.FALSE;
                List<MilestoneContract> milestoneContracts = contract.getPostRequest().getMilestoneContracts();
                for(MilestoneContract milestone : milestoneContracts) {
                    if(milestone.getStatus().equals(MilestoneStatus.SENDING)) {
                        checkAllComplete = Boolean.TRUE;
                    } else {
                        checkAllComplete = Boolean.FALSE;
                        break;
                    }
                }

                if(checkAllComplete) {
                    contract.setDeliveryStatus(DeliveryStatus.SENDING);
                    contractRepository.save(contract);
                }
                pushNotification.sendNotification(contract.getBuyer().getUser(),
                        WebConstant.DOMAIN + "/buyerhome/manageContract/" + contract.getId(),
                        "Bên đối tác đã tải lên bàn giao cho " + contract.getContractCode() + ", Kiếm tra ngay!");
//                if (contract.getExpectCompleteDate().compareTo(delivery.getCreateAt()) < 0) {
//                    contract.setDeliveryStatus(DeliveryStatus.LATE);
//                    contractRepository.save(contract);
//                }
                return new DeliveryHaveMilestoneResponse(update.getId(), update.getContract().getId(),
                        update.getMilestoneId(), update.getFile(), update.getDescription());
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bạn không thể bàn giao sản phẩm");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public DeliveryNotMilestoneResponse update(UUID deliveryId, DeliveryNotMilestoneRequest request, UserPrincipal currentUser) throws RuntimeException {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy đơn giao của bạn"));
        if (delivery.getContract().getDeliveryStatus().equals(DeliveryStatus.SENDING) &&
                delivery.getContract().getContractStatus().equals(ContractStatus.PROCESSING)) {
            if (delivery.getContract().getSeller().getUser().getId().equals(currentUser.getId())) {
                delivery.setFile(request.getFile());
                delivery.setDescription(request.getDescription());
                delivery.setUpdatedAt(new Date());
                Delivery update = deliveryRepository.save(delivery);
                return new DeliveryNotMilestoneResponse(update.getId(), update.getContract().getId(),
                        update.getFile(), update.getDescription());
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bạn không thể bàn giao sản phẩm");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
