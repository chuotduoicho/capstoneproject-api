package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PackageRequest {
    @NotBlank(message = "Tên tiêu đề gói không được để trống")
    @Size(max = 50, message = "Tiêu đề tối đa 50 ký tự")
    String title;

    @NotBlank(message = "Mô tả gói không được để trống")
    @Size(max = 500, message = "Mô tả cho gói của bạn cần tối đa 500 ký tự")
    String shortDescription;

    @NotNull(message = "Thời gian bàn giao package không được để trống")
    @Min(value = 1, message = "Thời gian tối thiểu bàn giao là 1 ngày")
    @Max(value = 360, message = "Số ngày bàn giao không quá 360 ngày")
    Integer deliveryTime;

    @NotNull(message = "Giá của package không được để trống")
    @Min(value = 1, message = "Giá của package của bạn tối thiểu là 1$")
    @Size(max = 10, message = "Chỉ được phép nhập tối đa 10 ký tự")
    BigDecimal price;

    @Min(value = 0, message = "Tối thiểu sẽ là 0%")
    @Max(value = 100, message = "Bạn chỉ được phép nhập tối đa 100% cho phí hủy hợp đồng")
    Integer contractCancelFee;
}
