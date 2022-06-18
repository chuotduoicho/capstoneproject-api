package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.model.Buyer;

import java.util.List;
import java.util.UUID;

public interface BuyerService {
    Buyer saveBuyer(Buyer buyer);
    List<Buyer> getBuyers();
    Buyer getBuyerById(UUID id);
    Buyer getBuyerByBuyerNumber(String buyerNumber);
    Buyer updateBuyer(Buyer buyer);
}
