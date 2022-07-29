package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.WalletRequest;
import com.jovinn.capstoneproject.dto.response.TransactionResponse;
import com.jovinn.capstoneproject.dto.response.WalletResponse;
import com.jovinn.capstoneproject.model.Transaction;
import com.jovinn.capstoneproject.model.Wallet;
import com.jovinn.capstoneproject.repository.payment.TransactionRepository;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.io.IOException;

public interface WalletService {
    String buyJCoin(WalletRequest request, UserPrincipal currentUser);
    WalletResponse getWallet(UserPrincipal currentUser);
    TransactionResponse saveWallet(String paymentId, String payerId, UserPrincipal currentUser) throws IOException;
    TransactionResponse getTransactionWallet(UserPrincipal currentUser);
}
