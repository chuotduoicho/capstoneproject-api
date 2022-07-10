package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.request.WalletRequest;
import com.jovinn.capstoneproject.dto.response.TransactionResponse;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.WalletService;
import com.jovinn.capstoneproject.service.payment.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("")
    public ResponseEntity<String> buyJCoin(@RequestBody WalletRequest request,
                           @CurrentUser UserPrincipal currentUser) {
        String response = walletService.buyJCoin(request, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "CANCEL PAYMENT";
    }

    @GetMapping("/success")
    public ResponseEntity<TransactionResponse> success(@RequestParam("paymentId") String paymentId,
                                                       @RequestParam("PayerID") String payerId,
                                                       @CurrentUser UserPrincipal currentUser) {
        TransactionResponse response = walletService.saveWallet(paymentId, payerId, currentUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
