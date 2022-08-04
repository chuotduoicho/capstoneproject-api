package com.jovinn.capstoneproject.dto.client.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtraOfferRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    String title;
    @NotBlank(message = "Bạn cần thêm mô tả cho extra offer")
    @Size(min = 30, max = 255, message = "Bạn cần nhập ít nhất 30 ký tự cho mô tả")
    String shortDescription;
    @NotNull(message = "Giá không được để trống")
    @Min(value = 1, message = "Giá trị nhập không được nhỏ hơn 1")
    BigDecimal extraPrice;
    @NotNull(message = "Ngày không được để trống")
    @Min(value = 1, message = "Giá trị nhập không được nhỏ hơn 1")
    Integer additionTime;
}
