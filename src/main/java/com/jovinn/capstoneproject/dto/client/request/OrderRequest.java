package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank UUID buyerId; //current user id
    @NotBlank UUID sellerId; // Id of buyer own package

    @NotBlank
    @Size(min = 20, max = 255)
    String requirement;

    @NotBlank
    Integer quantity;

    @NotBlank
    Integer totalDeliveryTime;

    Float totalPrice;

    @NotNull UUID userId;
    @NotNull UUID packageId;
}
