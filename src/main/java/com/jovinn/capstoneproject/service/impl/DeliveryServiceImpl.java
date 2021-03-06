package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.DeliveryHaveMilestoneRequest;
import com.jovinn.capstoneproject.dto.request.DeliveryNotMilestoneRequest;
import com.jovinn.capstoneproject.dto.response.ApiResponse;
import com.jovinn.capstoneproject.dto.response.DeliveryHaveMilestoneResponse;
import com.jovinn.capstoneproject.dto.response.DeliveryNotMilestoneResponse;
import com.jovinn.capstoneproject.enumerable.ContractStatus;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.exception.UnauthorizedException;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.Delivery;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.repository.DeliveryRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Override
    public DeliveryNotMilestoneResponse createDelivery(UUID id, DeliveryNotMilestoneRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found "));
        if (contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
            if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
                Delivery delivery = new Delivery(request.getFile(), request.getDescription(), contract);
                delivery.setCreateAt(new Date());
                delivery.setUpdatedAt(new Date());
                contract.setDeliveryStatus(DeliveryStatus.SENDING);
                contract.setUpdatedAt(new Date());
                contractRepository.save(contract);
                Delivery update = deliveryRepository.save(delivery);
//                if (contract.getExpectCompleteDate().compareTo(delivery.getCreateAt()) < 0) {
//                    contract.setDeliveryStatus(DeliveryStatus.LATE);
//                    contractRepository.save(contract);
//                }
                return new DeliveryNotMilestoneResponse(update.getId(), update.getContract().getId(),
                        update.getFile(), update.getDescription());
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "B???n kh??ng th??? b??n giao s???n ph???m");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public DeliveryHaveMilestoneResponse createDeliveryMilestone(UUID contractId, DeliveryHaveMilestoneRequest request, UserPrincipal currentUser) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Contract not found "));
        if (contract.getContractStatus().equals(ContractStatus.PROCESSING)) {
            if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
                Delivery delivery = new Delivery(request.getFile(), request.getDescription(), contract);
                delivery.setCreateAt(new Date());
                delivery.setUpdatedAt(new Date());
                delivery.setMilestoneId(request.getMilestoneId());
                contract.setDeliveryStatus(DeliveryStatus.SENDING);
                contract.setUpdatedAt(new Date());
                contractRepository.save(contract);
                Delivery update = deliveryRepository.save(delivery);
//                if (contract.getExpectCompleteDate().compareTo(delivery.getCreateAt()) < 0) {
//                    contract.setDeliveryStatus(DeliveryStatus.LATE);
//                    contractRepository.save(contract);
//                }
                return new DeliveryHaveMilestoneResponse(update.getId(), update.getContract().getId(),
                        update.getMilestoneId(), update.getFile(), update.getDescription());
            }
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "B???n kh??ng th??? b??n giao s???n ph???m");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }

    @Override
    public DeliveryNotMilestoneResponse update(UUID deliveryId, DeliveryNotMilestoneRequest request, UserPrincipal currentUser) throws RuntimeException {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Kh??ng t??m th???y ????n giao c???a b???n"));
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
            throw new ApiException(HttpStatus.BAD_REQUEST, "B???n kh??ng th??? b??n giao s???n ph???m");
        }

        ApiResponse apiResponse = new ApiResponse(Boolean.FALSE, "You don't have permission");
        throw new UnauthorizedException(apiResponse);
    }
}
