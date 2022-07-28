package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.response.AdminViewTransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<AdminViewTransactionResponse> getAllTransaction();

    List<AdminViewTransactionResponse> getAllTransactionByUserId(UUID userId);
}
