package com.jovinn.capstoneproject.dto.client.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LanguageResponse {
    @NotNull UUID id;
    String language;
    UUID sellerId;

    public LanguageResponse(UUID id, String language, UUID sellerId) {
        this.id = id;
        this.language = language;
        this.sellerId = sellerId;
    }
}
