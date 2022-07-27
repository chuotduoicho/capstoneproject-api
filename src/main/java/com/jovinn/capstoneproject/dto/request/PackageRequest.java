package com.jovinn.capstoneproject.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageRequest {
    @NotBlank(message = "Tên tiêu đề package không được để trống")
    String title;

    @NotBlank(message = "Mô tả package không được để trống")
    @Size(min = 30, max = 500, message = "Mô tả cho package của bạn cần tối thiểu 30 ký tự")
    String shortDescription;

    @NotBlank(message = "Thời gian bàn giao package không được để trống")
    @Min(value = 1, message = "Thời gian tối thiểu bàn giao là 1 ngày")
    Integer deliveryTime;

    @NotBlank(message = "Giá của package không được để trống")
    @Min(value = 1, message = "Giá của package của bạn tối thiểu là 1$")
    BigDecimal price;

    @Min(value = 0, message = "Tối thiểu sẽ là 0%")
    @Max(value = 100, message = "Bạn chỉ được phép nhập tối đa 100% cho phí hủy hợp đồng")
    Integer contractCancelFee;
}
