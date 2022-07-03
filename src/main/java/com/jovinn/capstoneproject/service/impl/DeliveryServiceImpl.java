package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.DeliveryRequest;
import com.jovinn.capstoneproject.dto.response.DeliveryResponse;
import com.jovinn.capstoneproject.enumerable.DeliveryStatus;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.Contract;
import com.jovinn.capstoneproject.model.Delivery;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.repository.ContractRepository;
import com.jovinn.capstoneproject.repository.DeliveryRepository;
import com.jovinn.capstoneproject.repository.SellerRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public DeliveryResponse updateDelivery(UUID id, DeliveryRequest request, UserPrincipal currentUser) {
        Seller seller = sellerRepository.findSellerByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "Seller not found ", currentUser.getId()));
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "Contract not found ", id));

        if (contract.getSeller().getUser().getId().equals(currentUser.getId())) {
            Delivery delivery = new Delivery(request.getContractId(), request.getFile(), request.getDescription(), contract);
            contract.setDeliveryStatus(DeliveryStatus.DELIVERY);
            contract.setUpdatedAt(new Date());
            contractRepository.save(contract);
            Delivery update = deliveryRepository.save(delivery);
            return new DeliveryResponse(update.getId(), update.getContract().getId(), update.getContract().getId(),
                    update.getFile(), update.getDescription());
        }
        return null;
    }
}
