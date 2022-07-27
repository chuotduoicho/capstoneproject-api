package com.jovinn.capstoneproject.service.impl;

import com.jovinn.capstoneproject.dto.response.AdminViewTransactionResponse;
import com.jovinn.capstoneproject.model.Transaction;
import com.jovinn.capstoneproject.model.User;
import com.jovinn.capstoneproject.repository.UserRepository;
import com.jovinn.capstoneproject.repository.payment.TransactionRepository;
import com.jovinn.capstoneproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
            user = new User();
            user = userRepository.findUserById(transaction.getUserId());
            responses.add(new AdminViewTransactionResponse(transaction.getId(), transaction.getUserId(), transaction.getAmount(),
                    transaction.getDescription(), transaction.getMethod(), transaction.getCurrency(), transaction.getPaymentCode(),
                    transaction.getMessage(), transaction.getType(), user.getFirstName() + " " + user.getLastName(), transaction.getCreateAt()));
        }
        return responses;
    }
}
