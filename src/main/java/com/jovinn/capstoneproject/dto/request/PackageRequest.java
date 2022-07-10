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
public class PackageRequest {
    @NotNull String title;
    @NotNull
    @Size(min = 20, max = 500)
    String shortDescription;
    @NotNull
    @Min(1)
    Integer deliveryTime;

    @NotNull
    @Min(1)
    BigDecimal price;

    @Min(0)
    @Max(100)
    Integer contractCancelFee;
}
