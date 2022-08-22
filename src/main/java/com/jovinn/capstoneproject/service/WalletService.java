package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.client.request.WalletRequest;
import com.jovinn.capstoneproject.dto.client.request.WithdrawAddressRequest;
import com.jovinn.capstoneproject.dto.client.response.ApiResponse;
import com.jovinn.capstoneproject.dto.client.response.TransactionResponse;
import com.jovinn.capstoneproject.dto.client.response.WalletResponse;
import com.jovinn.capstoneproject.security.UserPrincipal;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface WalletService {
    String buyJCoin(WalletRequest request, UserPrincipal currentUser);
    WalletResponse getWallet(UserPrincipal currentUser);
    TransactionResponse saveWallet(String paymentId, String payerId, UserPrincipal currentUser) throws IOException;
    TransactionResponse getTransactionWallet(UserPrincipal currentUser);
    ApiResponse addWithdrawAddress(WithdrawAddressRequest request, UserPrincipal currentUser);
    ApiResponse withdraw(WalletRequest request, UserPrincipal currentUser);
    List<TransactionResponse> getWithdrawRequestList(String year, String month);
    void exportCsvWithdraw(HttpServletResponse response) throws IOException;

    String buy();
}
