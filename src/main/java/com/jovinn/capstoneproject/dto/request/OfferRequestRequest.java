package com.jovinn.capstoneproject.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferRequestRequest {
    @NotNull
    @Size(min = 20, max = 255)
    String descriptionBio;
    @NotNull
    @Min(1)
    Integer totalDeliveryTime;
    @NotNull
    @Min(1)
    BigDecimal offerPrice;
    @NotNull
    @Min(0)
    @Max(100)
    Integer cancelFee; //tinh %
}
