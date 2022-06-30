package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.request.WalletRequest;
import com.jovinn.capstoneproject.dto.response.WalletResponse;
import com.jovinn.capstoneproject.exception.ResourceNotFoundException;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.model.Wallet;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.WalletRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;
import com.jovinn.capstoneproject.service.WalletService;
import com.jovinn.capstoneproject.service.payment.PaymentService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Override
    public String buyJCoin(UUID id, WalletRequest request, UserPrincipal currentUser) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "User not found ", request.getUserId()));
        Wallet wallet = walletRepository.findWalletByUserId(id);
        try {
            Payment payment = paymentService.createPayment(request.getWithdraw(), request.getCurrency(),
                    request.getMethod(), request.getIntent(), request.getDescription(),
                    "http://localhost:8080/", "http://localhost:8080/");
            wallet.setWithdraw(request.getWithdraw());
            wallet.setDescription(request.getDescription());
            for(Links link:payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }
}
