package com.jovinn.capstoneproject.dto.request;

import com.jovinn.capstoneproject.enumerable.ContractType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractRequest {
    @NotNull UUID packageId;
    @NotNull ContractType type;

    @NotNull
    @Min(0)
    @Max(100)
    Integer contractCancelFee;

    @NotBlank
    @Size(min = 30, max = 500)
    @NotNull String requirement;

    @NotNull Integer quantity;
}