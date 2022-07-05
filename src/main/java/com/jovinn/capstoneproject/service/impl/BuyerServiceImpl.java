package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.model.Buyer;
import com.jovinn.capstoneproject.repository.BuyerRepository;
import com.jovinn.capstoneproject.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private BuyerRepository buyerRepository;

    @Override
    public Buyer saveBuyer(Buyer buyer) {
        return buyerRepository.save(buyer);
    }
}
