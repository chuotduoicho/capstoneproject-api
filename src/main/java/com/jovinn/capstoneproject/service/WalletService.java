package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.request.WalletRequest;
import com.jovinn.capstoneproject.model.Wallet;
import com.jovinn.capstoneproject.security.UserPrincipal;

import java.util.UUID;

public interface WalletService {
    String buyJCoin(UUID id, WalletRequest request, UserPrincipal currentUser);
    Wallet getWallet(UserPrincipal currentUser);
}
