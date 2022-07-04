package com.jovinn.capstoneproject.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WalletRequest {
    @NotNull
    @Min(2)
    Double withdraw;
    @NotBlank
    String currency;
    @NotBlank
    String method;
    @NotBlank
    String intent;
    //Auto generate field text is: "You want to buy ** coin in Jovinn"
    @NotBlank
    String description;
    @NotNull
    UUID userId;
}
