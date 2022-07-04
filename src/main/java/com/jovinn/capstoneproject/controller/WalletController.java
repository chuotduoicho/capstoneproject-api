package com.jovinn.capstoneproject.controller;

import com.jovinn.capstoneproject.dto.request.WalletRequest;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.model.Wallet;
import com.jovinn.capstoneproject.repository.WalletRepository;
import com.jovinn.capstoneproject.security.CurrentUser;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.UserService;
import com.jovinn.capstoneproject.service.WalletService;
import com.jovinn.capstoneproject.service.payment.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = "*")
public class WalletController {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletService walletService;

//    @PostMapping("/pay-jcoin/{id}")
//    public String buyJCoin(@PathVariable("id") UUID id,
//                           @ModelAttribute("wallet") WalletRequest request,
//                           UserPrincipal currentUser) {
////        try {
////            Payment payment = paymentService.createPayment(request.getWithdraw(), request.getCurrency(),
////                    request.getMethod(), request.getIntent(), request.getDescription(),
////                    "http://localhost:8080/", "http://localhost:8080/");
////
////            Wallet wallet = walletRepository.findWalletByUserId(currentUser.getId());
////
////            for(Links link:payment.getLinks()) {
////                if(link.getRel().equals("approval_url")) {
////                    return "redirect:" + link.getHref();
////                }
////            }
////        } catch (PayPalRESTException e) {
////            e.printStackTrace();
////        }
//        return null;
//    }
    @GetMapping("/wallet")
    public ResponseEntity<Wallet> getWallet(@CurrentUser UserPrincipal currentUser) {
        Wallet response = walletService.getWallet(currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
