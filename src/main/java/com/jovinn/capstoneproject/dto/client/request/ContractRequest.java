package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractRequest {
    @NotNull UUID packageId;

    @NotBlank(message = "Yêu cầu của bạn không được để trống")
    @Size(max = 5000, message = "Yêu cầu tối đa 500 ký tự")
    String requirement;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Tối thiểu là 1 đơn vị số lượng")
    Integer quantity;
}
