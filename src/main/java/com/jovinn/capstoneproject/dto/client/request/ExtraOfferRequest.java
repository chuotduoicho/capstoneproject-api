package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtraOfferRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(max = 50, message = "Tiêu đề tối đa 50 ký tự")
    String title;

    @NotBlank(message = "Bạn cần thêm mô tả cho extra offer")
    @Size(max = 500, message = "Bạn chỉ được phép nhập tối đa 500 ký tự cho mô tả")
    String shortDescription;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 1, message = "Giá trị nhập không được nhỏ hơn 1")
    BigDecimal extraPrice;

    @NotNull(message = "Ngày không được để trống")
    @Min(value = 1, message = "Giá trị nhập không được nhỏ hơn 1")
    @Max(value = 360, message = "Số ngày bàn giao không quá 360 ngày")
    Integer additionTime;
}
