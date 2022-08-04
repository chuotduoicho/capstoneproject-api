package com.jovinn.capstoneproject.dto.client.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CertificateResponse {
    @NotNull UUID id;
    String title;
    String name;
    String linkCer;
    UUID sellerId;

    public CertificateResponse(UUID id, String title, String name, String linkCer, UUID sellerId) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.linkCer = linkCer;
        this.sellerId = sellerId;
    }
}
