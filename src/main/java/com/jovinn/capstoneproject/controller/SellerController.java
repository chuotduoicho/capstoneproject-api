package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Seller;
import com.jovinn.capstoneproject.model.Skill;
import com.jovinn.capstoneproject.repository.SkillRepository;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private SkillRepository skillRepository;

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

    //Using for update seller all information - But fuckin error
    @PutMapping("/profile/{id}")
    public ResponseEntity<Seller> updateInfo(@PathVariable("id") UUID id,
                                             @Valid @RequestBody Seller editSeller
                                             ) {
        Seller updateSeller = sellerService.updateSeller(id, editSeller);
        return new ResponseEntity< >(updateSeller, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Seller> update(@PathVariable("id") UUID id) {
        Seller seller = sellerService.getSellerById(id);
        return null;
    }
}
