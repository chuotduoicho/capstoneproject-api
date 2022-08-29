package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OfferRequestRequest {
    @NotBlank(message = "Mô tả sơ bộ thông tin của bạn cho lời đề nghỉ không được để trống")
    @Size(max = 500, message = "Lời mô tả đề nghị của bạn cần tối đa 500 ký tự")
    String descriptionBio;

    @NotBlank(message = "Ngày bàn giao không được để trống")
    @Min(value = 1, message = "Thời gian để bàn giao tối thiểu là 1 ngày")
    Integer totalDeliveryTime;

    @NotNull(message = "Giá đề nghị không được để trống")
    @Min(value = 1, message = "Giá thành đề nghị tối thiểu là 1$")
    BigDecimal offerPrice;

    @NotNull(message = "Phí hủy không được để trống")
    @Min(value = 0, message = "Tối thiểu sẽ là 0%")
    @Max(value = 100, message = "Bạn chỉ được phép nhập tối đa 100% cho phí hủy hợp đồng")
    Integer cancelFee; //tinh %
}
