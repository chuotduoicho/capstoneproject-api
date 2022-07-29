package com.jovinn.capstoneproject.repository.payment;

import com.jovinn.capstoneproject.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Transaction findTransactionByUserId(UUID userId);

    List<Transaction> findAllByOrderByCreateAtAsc();

    List<Transaction> findAllByWallet_User_Id(UUID userId);
}
