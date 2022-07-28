package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.response.AdminViewTransactionResponse;
import com.jovinn.capstoneproject.exception.ApiException;
import com.jovinn.capstoneproject.model.Transaction;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.payment.TransactionRepository;
import com.jovinn.capstoneproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public List<AdminViewTransactionResponse> getAllTransaction() {
        List<Transaction> transactions = transactionRepository.findAllByOrderByCreateAtAsc();
        List<AdminViewTransactionResponse> responses = new ArrayList<>();
        User user;
        for (Transaction transaction : transactions) {
            user = userRepository.findUserById(transaction.getUserId());
            responses.add(new AdminViewTransactionResponse(transaction.getId(), transaction.getUserId(), transaction.getAmount(),
                    transaction.getDescription(), transaction.getMethod(), transaction.getCurrency(), transaction.getPaymentCode(),
                    transaction.getMessage(), transaction.getType(), user.getFirstName() + " " + user.getLastName(), transaction.getCreateAt()));
        }
        return responses;
    }

    @Override
    public List<AdminViewTransactionResponse> getAllTransactionByUserId(UUID userId) {
        List<Transaction> transactions = transactionRepository.findAllByWallet_User_Id(userId);
        List<AdminViewTransactionResponse> responseList = new ArrayList<>();
        User user = userRepository.findUserById(userId);
        for (Transaction transaction:
             transactions) {
            responseList.add(new AdminViewTransactionResponse(transaction.getId(),transaction.getUserId(),transaction.getAmount(),
                    transaction.getDescription(),transaction.getMethod(),transaction.getCurrency(),transaction.getPaymentCode(),
                    transaction.getMessage(),transaction.getType(),user.getFirstName()+" "+user.getLastName(),transaction.getCreateAt()));
        }
        return responseList;
    }

    @Override
    public AdminViewTransactionResponse getTransactionById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Không tìm thấy giao dịch"));
        User user = userRepository.findUserById(transaction.getUserId());
        return new AdminViewTransactionResponse(transaction.getId(),transaction.getUserId(),transaction.getAmount()
        ,transaction.getDescription(),transaction.getMethod(),transaction.getCurrency(),transaction.getPaymentCode()
        ,transaction.getMessage(),transaction.getType(),user.getFirstName()+" "+user.getLastName()
        ,transaction.getCreateAt());
    }
}
