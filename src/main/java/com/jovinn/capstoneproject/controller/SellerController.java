package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.client.response.RatingResponse;
import com.jovinn.capstoneproject.dto.client.response.SellerSkillResponse;
import com.jovinn.capstoneproject.enumerable.SkillLevel;
import com.jovinn.capstoneproject.model.OfferRequest;
import com.jovinn.capstoneproject.model.Rating;
import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.OfferRequestService;
import com.jovinn.capstoneproject.service.RatingService;
import com.jovinn.capstoneproject.service.SellerService;
import com.jovinn.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller")
@CrossOrigin(origins = "*")
public class SellerController {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private OfferRequestService offerRequestService;
    @Autowired
    private SkillService skillService;

    @GetMapping("/sellers")
    public List<Seller> getListSellers() {
        return sellerService.getSellers();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Seller>> getListUsers() {
        List<Seller> sellers = sellerService.getSellers();
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public Seller getSellerProfile(@PathVariable UUID id) {
        return sellerService.getSellerById(id);
    }

    @GetMapping("/{brandName}")
    public Seller getSellerByBrandName(@PathVariable String brandName) {
        return sellerService.getSellerByBrandName(brandName);
    }
     @PutMapping("/profile")
     public ResponseEntity<Seller> updateInfo(@RequestBody Seller seller,
                                              @CurrentUser UserPrincipal currentUser) {
         Seller updateSeller = sellerService.updateSeller(seller, currentUser);
         return new ResponseEntity< >(updateSeller, HttpStatus.CREATED);
     }
    @GetMapping("/getTop3SellerByRank")
    public List<Seller> getTop3SellerByRank(){
        return sellerService.getListTopSellerByRank();
    }

    @GetMapping("/getSellerBySkillNameAndLevelOrderBySellerId/{names}/{level}")
    public List<SellerSkillResponse> getSellerBySkillNameAndLevelOrderBySellerId(@PathVariable List<String> names, @PathVariable SkillLevel level){
        return skillService.getSellerBySkillNameAndSkillLevelOrderBySellerId(names, level);
    }

    @GetMapping("/getSellerBySkillNameAndLevel/{names}/{level}")
    public List<Skill> getSellerBySkillNameAndLevel(@PathVariable List<String> names, @PathVariable SkillLevel level){
        return skillService.getSellerBySkillNameAndSkillLevel(names, level);
    }

    @GetMapping("/list-offer")
    public ResponseEntity<List<OfferRequest>> getOfferRequests(@CurrentUser UserPrincipal currentUser) {
        List<OfferRequest> response = offerRequestService.getOffers(currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
