package com.jovinn.capstoneproject.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletRequest {
    @NotNull
    @Min(1)
    BigDecimal charge;
    @NotBlank
    String currency;
    //Auto generate field text is: "You want to buy ** coin in Jovinn"
}
