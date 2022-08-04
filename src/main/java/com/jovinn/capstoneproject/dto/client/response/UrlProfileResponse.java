package com.jovinn.capstoneproject.dto.client.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UrlProfileResponse {
    @NotNull UUID id;
    String name;
    String url;
    UUID sellerId;

    public UrlProfileResponse(UUID id, String name, String url, UUID sellerId) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.sellerId = sellerId;
    }
}
