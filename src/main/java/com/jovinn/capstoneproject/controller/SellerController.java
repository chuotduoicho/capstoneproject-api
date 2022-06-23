package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.enumerable.RankSeller;
import com.jovinn.capstoneproject.enumerable.UserActivityType;
import com.jovinn.capstoneproject.model.ActivityType;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.auth.ActivityTypeRepository;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.SellerService;
import com.jovinn.capstoneproject.service.UserService;
import org.aspectj.internal.lang.reflect.StringToType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EnumType;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityTypeRepository activityTypeRepository;
    @GetMapping("/sellers")
    public List<Seller> getListSellers() {
        return sellerService.getSellers();
    }
    @GetMapping("/profile/{id}")
    public Seller getSellerProfile(@PathVariable UUID id) {
        return sellerService.getSellerById(id);
    }
    @PutMapping("/profile/{id}")
    public ResponseEntity<Seller> updateInfo(@PathVariable("id") UUID id,
                                             @RequestBody Seller seller,
                                             @CurrentUser UserPrincipal currentUser) {
        Seller updateSeller = sellerService.updateSeller(id, seller, currentUser);
        return new ResponseEntity< >(updateSeller, HttpStatus.CREATED);
    }
    @GetMapping("/getTop3SellerByRank")
    public List<Seller> getTop3SellerByRank(){
        return sellerService.getListTopSellerByRank();
    }
}
