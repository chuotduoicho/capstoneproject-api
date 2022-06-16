package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @PostMapping("/add-info-seller")
    public Seller addInfo(@RequestBody Seller seller) {
        return sellerService.saveSeller(seller);
    }

    @PostMapping("/update-info-seller")
    public Seller updateInfo(@RequestParam("id") UUID id, @RequestBody Seller seller) {
        return sellerService.updateSeller(seller, id);
    }
}
