package com.jovinn.capstoneproject.dto.request;

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
    @Size(min = 30, max = 500, message = "Yêu cầu cần đạt tối thiểu 30 ký tự và tối đa 500 ký tự")
    String requirement;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Tối thiểu là 1 đơn vị số lượng")
    Integer quantity;
}