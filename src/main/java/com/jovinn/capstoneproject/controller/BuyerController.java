package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.model.Buyer;
import com.jovinn.capstoneproject.service.BuyerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BuyerController {
    @Autowired
    private BuyerService buyerService;

    @GetMapping("buyers")
    public ResponseEntity<List<Buyer>> getBuyers() {
        return null;
    }
}
