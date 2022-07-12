package com.jovinn.capstoneproject.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferRequestResponse {
    UUID id;
    UUID postRequestId;
    String descriptionBio;
    Integer totalDeliveryTime;
    BigDecimal offerPrice;
    Integer cancelFee; //tinh %
    String message;
    public OfferRequestResponse(UUID id, UUID postRequestId,
                                String descriptionBio, Integer totalDeliveryTime,
                                BigDecimal offerPrice, Integer cancelFee,
                                String message) {
        this.id = id;
        this.postRequestId = postRequestId;
        this.descriptionBio = descriptionBio;
        this.totalDeliveryTime = totalDeliveryTime;
        this.offerPrice = offerPrice;
        this.cancelFee = cancelFee;
        this.message = message;
    }
}
