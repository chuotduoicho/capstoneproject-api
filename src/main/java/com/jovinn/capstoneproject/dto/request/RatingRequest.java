package com.jovinn.capstoneproject.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {
    @NotNull(message = "Vui lòng chọn mức độ hài lòng của bạn về sản phẩm")
    @Min(value = 1, message = "Tối thiểu là 1 sao đánh giá")
    @Max(value = 5, message = "Tối đa là 5 sao đánh giá")
    Integer ratingPoint;
    String comment;
}
