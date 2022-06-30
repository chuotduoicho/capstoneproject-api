package com.jovinn.capstoneproject.dto.request;

import com.jovinn.capstoneproject.enumerable.ContractType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractRequest {
    @NotNull UUID packageId;
    @NotNull UUID sellerId;
    @NotNull ContractType type;

    @NotBlank
    @Size(min = 30, max = 500)
    @NotNull String requirement;

    @NotNull Integer quantity;
    //Expect tai thoi diem accept boi Seller thi se update bang ngay hien tai + totalDeliveryTime de ra duoc ngay accept contract
    @NotNull Date expectCompleteDate;
}