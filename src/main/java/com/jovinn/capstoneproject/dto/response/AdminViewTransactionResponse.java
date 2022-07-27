package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.enumerable.TransactionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminViewTransactionResponse {
    UUID id;
    UUID userId;
    BigDecimal amount;
    String description;
    String method;
    String currency;
    String paymentCode;
    String message;
    TransactionType type;
    String fullName;
    Date dateCreated;

    public AdminViewTransactionResponse(UUID id, UUID userId, BigDecimal amount, String description, String method, String currency, String paymentCode, String message, TransactionType type, String fullName, Date dateCreated) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.method = method;
        this.currency = currency;
        this.paymentCode = paymentCode;
        this.message = message;
        this.type = type;
        this.fullName = fullName;
        this.dateCreated = dateCreated;
    }
}
