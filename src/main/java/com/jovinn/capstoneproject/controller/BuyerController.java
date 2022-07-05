package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Buyer;
import com.jovinn.capstoneproject.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buyer")
@CrossOrigin(origins = "*")
public class BuyerController {

    @Autowired
    private BuyerService buyerService;

    @PostMapping("/addBuyer")
    public Buyer addBuyer(@RequestBody Buyer buyer){
        return buyerService.saveBuyer(buyer);
    }
}
