package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.Buyer;
import com.jovinn.capstoneproject.repository.BuyerRepository;
import com.jovinn.capstoneproject.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private BuyerRepository buyerRepository;
    @Override
    public Buyer saveBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }

    @Override
    public List<Buyer> getBuyers() {
        return buyerRepository.findAll();
    }

    @Override
    public Buyer getBuyerById(UUID id) {
        return buyerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "Buyer not found", id));
    }

    @Override
    public Buyer getBuyerByBuyerNumber(String buyerNumber) {
        return buyerRepository.findByBuyerNumber(buyerNumber);
    }

    @Override
    public Buyer updateBuyer(Buyer buyer) {
        Buyer existBuyer = buyerRepository.findById(buyer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "Not found buyer", buyer));
        existBuyer.setSuccessContract(buyer.getSuccessContract());
        return buyerRepository.save(buyer);
    }
}
