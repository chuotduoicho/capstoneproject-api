package com.jovinn.capstoneproject.dto.response;

import com.jovinn.capstoneproject.enumerable.TransactionType;
import com.jovinn.capstoneproject.model.Transaction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionResponse {
    @NotNull UUID id;
    BigDecimal amount;
    String currency;
    String method;
    String intent;
    String description;
    String paymentCode;
    UUID userId;
    UUID walletId;
    String message;
    Date paymentAt;
    TransactionType type;

    public TransactionResponse(UUID id, BigDecimal amount, TransactionType type,
                               String currency, String method,
                               String intent, String description, String paymentCode,
                               UUID userId, String message, UUID walletId, Date paymentAt) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.currency = currency;
        this.method = method;
        this.intent = intent;
        this.description = description;
        this.paymentCode = paymentCode;
        this.userId = userId;
        this.walletId = walletId;
        this.message = message;
        this.paymentAt = paymentAt;
    }
}
