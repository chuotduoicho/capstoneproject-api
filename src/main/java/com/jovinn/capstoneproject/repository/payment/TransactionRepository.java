package com.jovinn.capstoneproject.repository.payment;

import com.jovinn.capstoneproject.enumerable.TransactionType;
import com.jovinn.capstoneproject.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Transaction findTransactionByUserId(UUID userId);

    List<Transaction> findAllByOrderByCreateAtAsc();

    List<Transaction> findAllByWallet_User_Id(UUID userId);

    @Query("SELECT t FROM Transaction t WHERE t.type = ?1 " +
            "AND (year(t.createAt) = year(current_date) and month(t.createAt) = month(current_date))")
    List<Transaction> findAllTransactionWithdrawRequest(TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.type = ?1 " +
            "AND (year(t.createAt) = ?2 and month(t.createAt) = ?3)")
    List<Transaction> findAllTransactionWithdraw(TransactionType type, Integer year, Integer month);
}
