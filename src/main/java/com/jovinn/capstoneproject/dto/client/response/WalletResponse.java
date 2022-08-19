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
    String withdrawAddress;
    List<Transaction> transactions;

    public WalletResponse(UUID id, BigDecimal income, BigDecimal withdraw, String withdrawAddress,
                          List<Transaction> transactions) {
        this.id = id;
        this.income = income;
        this.withdraw = withdraw;
        this.withdrawAddress = withdrawAddress;
        this.transactions = transactions;
    }
}
