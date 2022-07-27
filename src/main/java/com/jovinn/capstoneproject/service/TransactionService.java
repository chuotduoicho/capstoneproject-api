package com.jovinn.capstoneproject.service;

import com.jovinn.capstoneproject.dto.response.AdminViewTransactionResponse;

import java.util.List;

public interface TransactionService {
    List<AdminViewTransactionResponse> getAllTransaction();
}
