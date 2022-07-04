package com.jovinn.capstoneproject.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletResponse {
    @NotNull UUID id;
    Double withdraw;
    String currency;
    String method;
    String intent;
    String description;
    UUID userId;

    public WalletResponse(UUID id, Double withdraw,
                            String currency, String method,
                            String intent, String description,
                            UUID userId) {
        this.id = id;
        this.withdraw = withdraw;
        this.currency = currency;
        this.method = method;
        this.intent = intent;
        this.description = description;
        this.userId = userId;
    }
}
