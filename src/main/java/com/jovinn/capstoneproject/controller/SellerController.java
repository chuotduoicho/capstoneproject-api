package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.service.SellerService;
import com.jovinn.capstoneproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @GetMapping("/sellers")
    public Seller getListSellers() {
        return null;
    }
//    @PostMapping("/add-info-seller")
//    public Seller addInfo(@RequestBody Seller seller) {
//        return sellerService.saveSeller(seller);
//    }
    @PostMapping("/me/{id}/join-selling")
    public Seller addInfo(@PathVariable UUID id, @RequestBody Seller seller) {
        User user = userService.getByUserId(id);
        seller.setUser(user);
        seller.setRankSeller(RankSeller.BEGINNER);
        ActivityType at = activityTypeRepository.findByActivityType(UserActivityType.SELLER).get();
        at.setActivityType(UserActivityType.BUYER);
        user.setActivityType(Collections.singleton(at));
        user.setJoinSellingAt(new Date());
        return sellerService.saveSeller(seller);
    }
    @PutMapping("/me/seller/{id}")
    public ResponseEntity<Seller> updateInfo(@PathVariable("id") UUID id, @RequestBody Seller seller) {
        sellerService.updateSeller(id, seller);
        return new ResponseEntity<>(sellerService.getSellerById(id), HttpStatus.OK);
    }
}
