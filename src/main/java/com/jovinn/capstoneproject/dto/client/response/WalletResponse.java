package com.jovinn.capstoneproject.dto.client.response;

import com.jovinn.capstoneproject.model.Transaction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletResponse {
    UUID id;
    BigDecimal income;
    BigDecimal withdraw;
    List<Transaction> transactions;

    public WalletResponse(UUID id, BigDecimal income, BigDecimal withdraw, List<Transaction> transactions) {
        this.id = id;
        this.income = income;
        this.withdraw = withdraw;
        this.transactions = transactions;
    }
}
